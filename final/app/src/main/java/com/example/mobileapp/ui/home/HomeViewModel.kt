package com.example.mobileapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.dto.FoodQuoteDto
import com.example.mobileapp.network.NinjaApi
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _quote = MutableLiveData<FoodQuoteDto>()

    val quote: LiveData<FoodQuoteDto> = _quote

    init {
        getFoodQuote()
    }
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