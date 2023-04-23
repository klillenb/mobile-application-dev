package com.example.mobileapp.ui.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mobileapp.dto.RecipeDto
import com.example.mobileapp.repository.RecipeRepository

class RecipesViewModel : ViewModel() {
    //seda peaks kõike äkki keskselt tegema, et ei oleks kogu aeg uuesi laadimist
    //Kõikide fragmentide peale äkki üks ViewModel?
    private val repository = RecipeRepository()

    val liveData: LiveData<List<RecipeDto>> = repository.recipes
    fun getData() { repository.loadRecipes() }

}