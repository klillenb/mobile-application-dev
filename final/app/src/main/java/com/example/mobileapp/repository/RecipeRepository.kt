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
//    fun loadRecipes(){
//
//        val recipeDtoList = List(10){ i -> RecipeDto(name="Recipe${i+1}", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.") }
//        _recipes.value = recipeDtoList
//    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun getRecipes() {
        launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    RecipeApi.retrofitService.getRecipes()
                }


                //siin tuleks filtreerida: kui lemmikute nimekirjas, siis fave = true



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
        Log.d("STATUS", "Inf pärist vajutust ${recipes.value?.find { it._id == recipe._id}}")

        // kui fave on false siis lisada lemmikutesse

        // kui fave on true siis eemaldada lemmikutest

    }


    fun checkFaves() {
        //lugeda lemmikutest
    }
}