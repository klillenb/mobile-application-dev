package com.example.mobileapp.repository


import android.content.Context
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

    init {
        faveRecipes += loadFaves("fave_recipes", faveRecipes)
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
                }
                _recipes.value = result
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun toggleFave(recipe: RecipeDto){
        _recipes.value?.find { it._id == recipe._id}?.fave = !recipe.fave
        this._recipes.postValue(_recipes.value)

        if (recipe.fave){
            faveRecipes.add(recipe._id)
        } else {
            faveRecipes.remove(recipe._id)
        }
        saveFaves("fave_recipes", faveRecipes)
        //Log.d("STATUS", "SP ${loadFaves("fave_recipes", faveRecipes)}")
    }

    private fun saveFaves(key: String, list: List<String>) {
        val serializedList = gson.toJson(list)
        sharedPreferences.edit().putString(key, serializedList).apply()
    }

    private fun loadFaves(key: String, defaultValue: List<String>): List<String> {
        val serializedList = sharedPreferences.getString(key, null)
        return if (serializedList != null) {
            gson.fromJson(serializedList, object : TypeToken<List<String>>() {}.type)
        } else {
            defaultValue
        }
    }
}