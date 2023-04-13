package com.example.mobileapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.network.NinjaApi
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _status = MutableLiveData<String>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    init {
        getFoodQuote()
    }

    val text: LiveData<String> = _text
    val status: LiveData<String> = _status

    private fun getFoodQuote() {
        viewModelScope.launch {
            try {
                val result = NinjaApi.retrofitService.getQuote(getHeaderMap())
                _status.value = result
                println(result)
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            }
        }
    }

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["X-Api-Key"] = "${NinjaApi.apiKey}"
        return headerMap
    }
}