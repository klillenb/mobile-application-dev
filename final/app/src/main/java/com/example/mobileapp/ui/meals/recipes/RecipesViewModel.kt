package com.example.mobileapp.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileapp.repository.Recipe
import com.example.mobileapp.repository.RecipeRepository

class RecipesViewModel : ViewModel() {

    private val repository = RecipeRepository()

    val liveData: LiveData<List<Recipe>> = repository.recipes
    fun getData() { repository.loadRecipes() }

/*    private val _text = MutableLiveData<String>().apply {
        value = "This is recipes Fragment"
    }
    val text: LiveData<String> = _text*/

}