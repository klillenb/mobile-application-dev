package com.example.mobileapp.dto

data class RecipeDto(
    val _id: String,
    val name: String,
    val ingredients: List<String>,
    val instructions: String,
    val username: String,
    val description: String,
    val picture: String?,
    var fave: Boolean = false,
    var inCart: Boolean = false,
)

/*
{
    name: { type: String, required: true },
    //ingrediets is an array of objects with name and quantity
    ingredients: { type: Array, required: true },
    instructions: { type: String, required: true },
    username: { type: String, required: true },
    description: { type: String, required: true },
    picture: { type: String, required: false },
}
*/
