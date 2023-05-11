package com.example.mobileapp.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mobileapp.dto.RecipeDto
import com.example.mobileapp.network.RecipeApi
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


//https://chris-ribetti.medium.com/android-viewmodel-livedata-repository-and-di-complete-and-super-quick-5a7d78fa7946

class RecipeRepository : CoroutineScope {
    private val job = Job()
    private val _recipes: MutableLiveData<List<RecipeDto>> = MutableLiveData()
    val recipes: LiveData<List<RecipeDto>> = _recipes

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun getRecipes() {
        launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    RecipeApi.retrofitService.getRecipes()
                }

                _recipes.value = result
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun toggleFave(recipe: RecipeDto){
        Log.d("STATUS", "Algne inf repos: ${_recipes.value?.find { it._id == recipe._id}}")
        _recipes.value?.find { it._id == recipe._id}?.fave = !recipe.fave
        this._recipes.postValue(_recipes.value)
        //Log.d("STATUS", "Inf pärist vajutust ${recipes.value?.find { it._id == recipe._id}}")

        // kas lisada serverisse või kohalikule kettale

    }
}