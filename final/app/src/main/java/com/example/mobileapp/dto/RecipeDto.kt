package com.example.mobileapp.dto

data class RecipeDto(
    val name: String,
    val ingredients: List<String>,
    val instructions: String,
    val username: String,
    val description: String,
    val picture: String?,
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
