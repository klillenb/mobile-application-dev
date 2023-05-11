package com.example.mobileapp.dto

data class RecipeDto(
    val _id: String,
    val name: String,
    val ingredients: List<String>,
    val instructions: String,
    val description: String,
    val image: String?,
    var fave: Boolean = false,
    var inCart: Boolean = false,
)
