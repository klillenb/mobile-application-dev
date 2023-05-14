package com.example.mobileapp.model

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.mobileapp.dto.RecipeDto
import com.example.mobileapp.repository.RecipeRepository
import com.example.mobileapp.dto.FoodQuoteDto
import com.example.mobileapp.network.RecipeApi
import kotlinx.coroutines.launch

//https://developer.android.com/codelabs/basic-android-kotlin-training-shared-viewmodel#3

class SharedViewModel(application: Application): AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context: Context = getApplication<Application>().applicationContext
    private val _repository = RecipeRepository(context)
    val recipeList: LiveData<List<RecipeDto>> = _repository.recipes

    private val _quote = MutableLiveData<FoodQuoteDto>()
    val quote: LiveData<FoodQuoteDto> = _quote

    init {
        getFoodQuote()
        getData()
    }

    //recipes
    private fun getData() { _repository.getRecipes() }

    fun toggleFave(pos: Int) {
        _repository.toggleFave(recipeList.value !! [pos])
    }

    fun toggleAddToCart(pos: Int) {
        _repository.toggleAddToCart(recipeList.value !! [pos])
    }

    fun removeFromCart(pos: Int) {
        _repository.removeFromCart(recipeList.value !! [pos])
    }

    //quotes
    private fun getFoodQuote() {
        viewModelScope.launch {
            try {
                val result = RecipeApi.retrofitService.getQuote()
                _quote.value = result[0]
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}