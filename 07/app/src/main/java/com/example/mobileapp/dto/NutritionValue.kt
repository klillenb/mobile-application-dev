package com.example.mobileapp.dto

import com.squareup.moshi.Json

data class NutritionValue (
    val name: String,
    val calories: Double,
    @Json(name = "serving_size_g") val servingSize: Int,
    @Json(name = "fat_total_g") val fatTotal: Double,
    @Json(name = "protein_g") val protein: Double,
    @Json(name = "sugar_g") val sugar: Double
)