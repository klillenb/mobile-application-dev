package com.example.mobileapp.network

import com.example.mobileapp.dto.FoodQuoteDto
import com.example.mobileapp.dto.RecipeDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

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
    /**
     * GET all recipes from DB
     * @return Response object, use .code() method for HTTP status code, .body() for response data
     */
    @GET("recipe")
    suspend fun getRecipes(): Response<List<RecipeDto>>

    /**
     * GET food related quote from server
     * @return Response object, use .code() method for HTTP status code, .body() for response data
     */
    @GET("quote")
    suspend fun getQuote(): Response<List<FoodQuoteDto>>

    /**
     * POST new recipe to DB
     * @param recipeData the new recipe to be inserted
     * @return Response object, use .code() and .body() methods for request result
     */
    @Headers("Content-Type: application/json")
    @POST("recipe/add")
    suspend fun addRecipe(@Body recipeData: RecipeDto): Response<String>

    /**
     * DELETE recipe from DB
     * @param recipeId id of recipe to remove
     * @return Response object, use .code() and .body() methods for request result
     */
    @DELETE("recipe/{id}")
    suspend fun removeRecipe(@Path("id") recipeId: String): Response<String>
}