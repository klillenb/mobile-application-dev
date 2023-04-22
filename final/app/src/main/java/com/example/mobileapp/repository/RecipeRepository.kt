package com.example.mobileapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

//https://chris-ribetti.medium.com/android-viewmodel-livedata-repository-and-di-complete-and-super-quick-5a7d78fa7946

class RecipeRepository {
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes
    fun loadRecipes(){
/*        var recipe1 = Recipe()
        recipe1.name = "Retsept1"
        recipe1.description = "Retsepti kirjeldus"
        var recipe2 = Recipe()
        recipe2.name = "Retsept2"
        recipe2.description = "Retsepti kirjeldus"*/

        val recipeList = List(2){_ -> Recipe(name="Recipe1", description = "Description1")}
        _recipes.value = recipeList
    }

}