package com.example.mobileapp.ui.shopping

data class ShoppingCartItem(
    val name: String,
    val recipeId: String,
    var done: Boolean = false,
)