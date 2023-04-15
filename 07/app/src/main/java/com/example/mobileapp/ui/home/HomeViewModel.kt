package com.example.mobileapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.network.NinjaApi
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _quote = MutableLiveData<String>()
    private val _author = MutableLiveData<String>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    init {
        getFoodQuote()
    }

    val text: LiveData<String> = _text
    val quote: LiveData<String> = _quote
    val author: LiveData<String> = _author

    private fun getFoodQuote() {
        viewModelScope.launch {
            try {
                val result = NinjaApi.retrofitService.getQuote(getHeaderMap())
                _quote.value = result[0].quote
                _author.value = result[0].author
                println(quote.value)
                println(author.value)
            } catch (e: Exception) {
                _quote.value = "Failure: ${e.message}"
            }
        }
    }

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["X-Api-Key"] = NinjaApi.apiKey
        return headerMap
    }
}