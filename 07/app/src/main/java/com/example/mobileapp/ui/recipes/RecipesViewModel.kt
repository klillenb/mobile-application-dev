package com.example.mobileapp.ui.recipes

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.dto.NutritionValue
import com.example.mobileapp.network.NinjaApi
import kotlinx.coroutines.launch
import kotlin.reflect.jvm.internal.impl.resolve.constants.DoubleValue

class RecipesViewModel : ViewModel() {

    private val _nutrition = MutableLiveData<NutritionValue>()
    val nutrition: LiveData<NutritionValue> = _nutrition

    var _status = MutableLiveData<Boolean?>()

    fun findNutrition(foodItem: String) {
        viewModelScope.launch {
            try {
                val result = NinjaApi.retrofitService.getNutrition(getHeaderMap(), foodItem)
                _status.value = true
                _nutrition.value = result[0]
            } catch (e: Exception) {
                _status.value = false
            }
        }
    }

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["X-Api-Key"] = NinjaApi.apiKey
        return headerMap
    }
}