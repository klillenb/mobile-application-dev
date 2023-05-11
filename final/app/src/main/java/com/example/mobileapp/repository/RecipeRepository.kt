package com.example.mobileapp.repository


import android.content.Context
//import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mobileapp.dto.RecipeDto
import com.example.mobileapp.network.RecipeApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


//https://chris-ribetti.medium.com/android-viewmodel-livedata-repository-and-di-complete-and-super-quick-5a7d78fa7946

class RecipeRepository(context: Context) : CoroutineScope {
    private val job = Job()
    private val _recipes: MutableLiveData<List<RecipeDto>> = MutableLiveData()
    val recipes: LiveData<List<RecipeDto>> = _recipes

    private val sharedPreferences = context.getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val faveRecipes = mutableListOf<String>()
    private val recipesInCart = mutableListOf<String>()

    init {
        faveRecipes += loadData("fave_recipes", faveRecipes)
        recipesInCart += loadData("recipes_in_cart", recipesInCart)
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun getRecipes() {
        launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    RecipeApi.retrofitService.getRecipes()
                }

                //kas on kohalikud lemmikud
                for (item: RecipeDto in result) {
                    if(faveRecipes.contains(item._id)){
                        item.fave=true
                    }

                    if(recipesInCart.contains(item._id)){
                        item.inCart=true
                    }
                }
                _recipes.value = result
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun toggleFave(recipe: RecipeDto){
        //Log.d("STATUS", "toggleFave")
        _recipes.value?.find { it._id == recipe._id}?.fave = !recipe.fave
        this._recipes.postValue(_recipes.value)

        if (recipe.fave){
            faveRecipes.add(recipe._id)
        } else {
            faveRecipes.remove(recipe._id)
        }
        saveData("fave_recipes", faveRecipes)
        //Log.d("STATUS", "Kärus olevad retseptid ${loadData("recipes_in_cart", recipesInCart)}")
        //Log.d("STATUS", "Lemmikud retseptid ${loadData("fave_recipes", faveRecipes)}")
    }

    fun toggleAddToCart(recipe: RecipeDto){
        //Log.d("STATUS", "toggleAddToCart")
        _recipes.value?.find { it._id == recipe._id}?.inCart = !recipe.inCart
        this._recipes.postValue(_recipes.value)

        if (recipe.inCart){
            recipesInCart.add(recipe._id)
        } else {
            recipesInCart.remove(recipe._id)
        }
        saveData("recipes_in_cart", recipesInCart)
        //Log.d("STATUS", "Kärus olevad retseptid ${loadData("recipes_in_cart", recipesInCart)}")
        //Log.d("STATUS", "Lemmikud retseptid ${loadData("fave_recipes", faveRecipes)}")
    }

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
}