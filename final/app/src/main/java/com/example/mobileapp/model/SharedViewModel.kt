package com.example.mobileapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.dto.RecipeDto
import com.example.mobileapp.repository.RecipeRepository
import com.example.mobileapp.dto.FoodQuoteDto
import com.example.mobileapp.network.NinjaApi
import kotlinx.coroutines.launch

//https://developer.android.com/codelabs/basic-android-kotlin-training-shared-viewmodel#3

class SharedViewModel: ViewModel() {
    private val _repository = RecipeRepository()
    val recipeList: LiveData<List<RecipeDto>> = _repository.recipes

    private val _quote = MutableLiveData<FoodQuoteDto>()
    val quote: LiveData<FoodQuoteDto> = _quote

    init {
        getFoodQuote()
        getData()
    }

    //recipes
    private fun getData() { _repository.getRecipes() }

    //quotes
    private fun getFoodQuote() {
        viewModelScope.launch {
            try {
                val result = NinjaApi.retrofitService.getQuote(getHeaderMap())
                _quote.value = result[0]
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["X-Api-Key"] = NinjaApi.apiKey
        return headerMap
    }

}