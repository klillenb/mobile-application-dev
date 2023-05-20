package com.example.mobileapp.dto

data class ShoppingCartDto(
    val name: String,
    val recipeId: String,
    var done: Boolean = false,
)