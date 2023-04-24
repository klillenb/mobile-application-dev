package com.example.mobileapp.network

import com.example.mobileapp.BuildConfig
import com.example.mobileapp.dto.FoodQuoteDto
import com.example.mobileapp.dto.RecipeDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap

private const val BASE_URL = "https://recipe-api.cyclic.app/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object RecipeApi {
    val retrofitService: RecipeApiService by lazy {
        retrofit.create(RecipeApiService::class.java)
    }
}

interface RecipeApiService {
    @GET("recipe")
    suspend fun getRecipes(): List<RecipeDto>
}