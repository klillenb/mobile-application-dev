package com.example.mobileapp.network

import com.example.mobileapp.BuildConfig
import com.example.mobileapp.dto.FoodQuote
import com.example.mobileapp.dto.NutritionValue
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

private const val BASE_URL = "https://api.api-ninjas.com/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object NinjaApi {
    const val apiKey = BuildConfig.API_KEY
    val retrofitService: NinjaApiService by lazy {
        retrofit.create(NinjaApiService::class.java)
    }
}

interface NinjaApiService {
    @GET("quotes?category=food")
    suspend fun getQuote(
        @HeaderMap headers: Map<String, String>
    ): List<FoodQuote>

    @GET("nutrition")
    suspend fun getNutrition(
        @HeaderMap headers: Map<String, String>,
        @Query("query") foodItem: String
    ): List<NutritionValue>
}