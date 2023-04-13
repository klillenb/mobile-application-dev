package com.example.mobileapp.network

import com.example.mobileapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap

private const val BASE_URL = "https://api.api-ninjas.com/v1/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
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
    ): String
}