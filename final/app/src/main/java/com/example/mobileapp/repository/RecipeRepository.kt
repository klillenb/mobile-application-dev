package com.example.mobileapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mobileapp.dto.RecipeDto

//https://chris-ribetti.medium.com/android-viewmodel-livedata-repository-and-di-complete-and-super-quick-5a7d78fa7946

class RecipeRepository {
    private val _recipes = MutableLiveData<List<RecipeDto>>()
    val recipes: LiveData<List<RecipeDto>> = _recipes
    fun loadRecipes(){

        val recipeDtoList = List(10){ i -> RecipeDto(name="Recipe${i+1}", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.") }
        _recipes.value = recipeDtoList
    }

}