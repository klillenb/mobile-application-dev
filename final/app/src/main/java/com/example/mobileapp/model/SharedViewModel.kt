package com.example.mobileapp.model

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
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
    val showProgress: MutableLiveData<Boolean> = MutableLiveData()

    private val _quote = MutableLiveData<FoodQuoteDto>()
    val quote: LiveData<FoodQuoteDto> = _quote

    init {
        getFoodQuote()
        getData()
    }

    fun toggleFave(pos: Int) {
        _repository.toggleFave(recipeList.value!![pos])
    }

    fun toggleAddToCart(pos: Int) {
        _repository.toggleAddToCart(recipeList.value!![pos])
    }

    //recipes
    fun getData() { _repository.getRecipes(context) }
    fun saveData(recipeDto: RecipeDto) { _repository.addRecipe(context, recipeDto) }
    fun removeData(recipeDto: RecipeDto) {_repository.removeRecipe(context, recipeDto) }

    //quotes
    private fun getFoodQuote() {
        viewModelScope.launch {
            try {
                showProgress.postValue(true)
                val result = RecipeApi.retrofitService.getQuote()
                if(result.code() == 200){ // .isSuccessful is any code in range 200-300, body is on HTTP 200
                    _quote.value = result.body()?.get(0)
                } else {
                    Toast.makeText(context, "Network error!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                println(e)
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            } finally {
                showProgress.postValue(false)
            }
        }
    }
}