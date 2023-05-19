package com.example.mobileapp.dto

data class ShoppingCartDto(
    val name: String,
    val recipeIndex: Int,
    var done: Boolean = false,
)