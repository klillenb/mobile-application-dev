package com.example.mobileapp.repository

data class Recipe(
    val name: String,
    val ingredients: List<Ingredient>? = null,
    val instructions: String? = null,
    val username: String? = null,
    val description: String? = null,
    val picture: String? = null,
)

/*
Reydani ja Kairi algne mudel
const recipeSchema = new Schema({
    name: { type: String, required: true },
    //ingrediets is an array of objects with name and quantity
    ingredients: { type: Array, required: true },
    instructions: { type: String, required: true },
    username: { type: String, required: true },
    description: { type: String, required: true },
    picture: { type: String, required: false },

}, {
    timestamps: true,
});*/
