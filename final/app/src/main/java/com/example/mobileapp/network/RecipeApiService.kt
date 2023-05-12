package com.example.mobileapp.network

import com.example.mobileapp.dto.FoodQuoteDto
import com.example.mobileapp.dto.RecipeDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://recipe-api.cyclic.app/"

/*
    dev url when running server on localhost
    add this to manifest for dev env -> android:usesCleartextTraffic="true"
*/
// private const val BASE_URL = "http://10.0.2.2:5000/"

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
    suspend fun getRecipes(): Response<List<RecipeDto>>

    @GET("quote")
    suspend fun getQuote(): Response<List<FoodQuoteDto>>
}