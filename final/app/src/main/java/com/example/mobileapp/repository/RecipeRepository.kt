package com.example.mobileapp.repository


import android.content.Context

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mobileapp.dto.RecipeDto
import com.example.mobileapp.dto.ShoppingCartDto
import com.example.mobileapp.network.RecipeApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Repository to contain request handling logic related to recipe data.
 * Also manages recipe saving / loading to / from sharedPreferences.
 */
class RecipeRepository(context: Context) : CoroutineScope {
    private val job = Job()
    private val _recipes: MutableLiveData<List<RecipeDto>> = MutableLiveData()
    val recipes: LiveData<List<RecipeDto>> = _recipes

    private val showProgress: MutableLiveData<Boolean> = MutableLiveData()

    private val sharedPreferences = context.getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val faveRecipes = mutableListOf<String>()
    private val recipesInCart = mutableListOf<String>()

    private val _shoppingCartItems: MutableLiveData<List<ShoppingCartDto>> = MutableLiveData()
    val shoppingCartItems: LiveData<List<ShoppingCartDto>> = _shoppingCartItems

    init {
        faveRecipes += loadData("fave_recipes", faveRecipes)
        recipesInCart += loadData("recipes_in_cart", recipesInCart)
        showProgress.value = true
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun getRecipes(context: Context) {
        launch {
            try {
                showProgress.value = true
                val result = withContext(Dispatchers.IO) {
                    RecipeApi.retrofitService.getRecipes()
                }
                if(result.code() == 200){
                    // check for local favourites
                    for (item: RecipeDto in result.body()!!) {
                        if(faveRecipes.contains(item._id)){
                            item.fave = true
                        }
                        if(recipesInCart.contains(item._id)){
                            item.inCart = true
                        }
                    }
                    _recipes.value = result.body()

                    //täidab ostukäru koostisosadega
                    getShoppingCartItems()
                } else if (result.code() == 500) {
                    Toast.makeText(context, "Internal server error!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Network error!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                println(e)
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            } finally {
                //getShoppingCartItems()
                //Log.d("RECIPE", "init cart ${recipeCartItems.value}")
                showProgress.postValue(false)
            }
        }
    }

    fun addRecipe(context: Context, recipe: RecipeDto) {
        launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    RecipeApi.retrofitService.addRecipe(recipe)
                }
                if (result.code() == 200) {
                    Toast.makeText(context, result.body(), Toast.LENGTH_SHORT).show()
                } else if (result.code() == 413) {
                    Toast.makeText(context, "Response from server: Image is too large!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, result.body(), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                //println(e.printStackTrace())
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun removeRecipe(context: Context, recipe: RecipeDto) {
        launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    recipe._id?.let { RecipeApi.retrofitService.removeRecipe(it) }
                }
                if (result != null) {
                    if (result.code() == 200) {
                        Toast.makeText(context, result.body(), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, result.body(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun toggleFave(recipe: RecipeDto){
        _recipes.value?.find { it._id == recipe._id}?.fave = !recipe.fave
        this._recipes.postValue(_recipes.value)

        if (recipe.fave){
            recipe._id?.let { faveRecipes.add(it) }
        } else {
            faveRecipes.remove(recipe._id)
        }
        saveData("fave_recipes", faveRecipes)
    }

    fun toggleAddToCart(recipe: RecipeDto, removeFromCart: Boolean = false) {

        //muudan retsepti välja väärtust
        _recipes.value?.find {
            it._id == recipe._id
        }!!.inCart = if (removeFromCart) false else (!recipe.inCart)
        this._recipes.postValue(_recipes.value)

        //lisan/kustutan retsepti kärust
        if (recipe.inCart){
            //lisan retsepti
            recipe._id?.let { recipesInCart.add(it) }
            //lisan koostisosad
            addIngredientsToShoppingCart(recipe)

        } else {
            //eemaldan retsepti
            recipesInCart.remove(recipe._id)
            //eemaldan koostisosad
            for(item in _shoppingCartItems.value!!){
                if(item.recipeId==recipe._id){
                    val currentList = _shoppingCartItems.value?.toMutableList()
                    currentList?.remove(item)
                    _shoppingCartItems.value = currentList!!
                    //Log.d("RECIPE", "Kustutati koostisosa $item list sai ${_shoppingCartItems.value}")
                }
            }
        }
        saveData("recipes_in_cart", recipesInCart)
        //Log.d("RECIPE", "Retseptid ostukärus $recipesInCart")
    }

    //SharedPreference'isse salvestamine ja sealt lugemine andmete seriliseerimise abil
    private fun saveData(key: String, list: List<String>) {
        val serializedList = gson.toJson(list)
        sharedPreferences.edit().putString(key, serializedList).apply()
    }

    private fun loadData(key: String, defaultValue: List<String>): List<String> {
        val serializedList = sharedPreferences.getString(key, null)
        return if (serializedList != null) {
            gson.fromJson(serializedList, object : TypeToken<List<String>>() {}.type)
        } else {
            defaultValue
        }
    }

    private fun getShoppingCartItems(){
        for (recipe: RecipeDto in recipes.value!!) {
            if (recipe.inCart) {
                addIngredientsToShoppingCart(recipe)
            }
        }
    }

    private fun addIngredientsToShoppingCart(recipe: RecipeDto){
        //Log.d("RECIPE", "Hakkan lisama koostisosi ${_shoppingCartItems.value}")
        recipe.ingredients.forEach {
                ingredient: String ->
            val item = ShoppingCartDto(
                ingredient,
                recipe._id!!
            )
            val currentList: MutableList<ShoppingCartDto> = _shoppingCartItems.value?.toMutableList() ?: mutableListOf()
            currentList.add(item)
            _shoppingCartItems.value = currentList

        }
        //Log.d("RECIPE", "Lisatud koostisosad ${_shoppingCartItems.value}")
    }

    fun removeIngredientsFromShoppingCart(shoppingCartItem: ShoppingCartDto){

        //eemaldab üksiku koostisosa kärust
        val currentList = _shoppingCartItems.value?.toMutableList()
        currentList?.remove(shoppingCartItem)
        _shoppingCartItems.value = currentList!!
        //Log.d("RECIPE", "Kustutati retsept $shoppingCartItem list sai ${_shoppingCartItems.value}")

        val recipe = recipes.value?.find { it._id == shoppingCartItem.recipeId }
        //eemaldab retsepti kärust
        recipesInCart.remove(recipe?._id)

        //muudab retsepti välja
        if(recipe != null && recipe.inCart){
            _recipes.value?.find {
                it._id == shoppingCartItem.recipeId
            }!!.inCart = false
        }
        //this._recipes.postValue(_recipes.value)
    }

    fun markChecked(item: ShoppingCartDto, checked: Boolean) {
        _shoppingCartItems.value?.find { it == item }?.done = checked
        //Log.d("RECIPE", "Koostisosad ${_shoppingCartItems.value}")
    }

    fun deleteDoneItems() {
        // kui item.done, siis eemaldada ostukärust ja retseptidest
        for( shoppingCartItem in _shoppingCartItems.value!!){
            if(shoppingCartItem.done){
                val currentList = _shoppingCartItems.value?.toMutableList()
                currentList?.remove(shoppingCartItem)
                _shoppingCartItems.value = currentList!!
                val recipe = recipes.value?.find { it._id == shoppingCartItem.recipeId }
                if(recipe != null && recipe.inCart){
                    //muudab retsepti välja
                    _recipes.value?.find {
                        it._id == recipe._id
                    }!!.inCart = false
                    //eemaldab retsepti kärust
                    recipesInCart.remove(recipe._id)
                }
            }
        }
    }

    fun completeAllItems() {
        _shoppingCartItems.value?.forEach { item ->
            item.done = true
        }
        //Log.d("RECIPE", "${shoppingCartItems.value}")
    }
}