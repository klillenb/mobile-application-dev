package com.example.mobileapp.dto

/**
 * Data class to contain information about received and new recipes
 * @property fave Relevant for application logic, not forwarded to server
 * @property inCart Relevant for application logic, not forwarded to server
 */
data class RecipeDto(
    val _id: String,
    val name: String,
    val ingredients: List<String>,
    val instructions: String,
    val description: String,
    val image: String?,
    @Transient
    var fave: Boolean = false,
    @Transient
    var inCart: Boolean = false,
)
