package com.example.cobarecipesapp.data

import com.example.cobarecipesapp.model.Category
import com.example.cobarecipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException


class RecipesRepository {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    //recipe by ID
    fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            val recipeResponse = service.getRecipeById(recipeId).execute()
            recipeResponse.body()
        } catch (e: IOException) {
            null
        }
    }

    //recipes by ID
    fun getRecipesByIds(favoritesId: String): List<Recipe>? {
        return try {
            val recipesResponse = service.getRecipesByIds(favoritesId).execute()
            recipesResponse.body()
        } catch (e: IOException) {
            null
        }
    }

    //category by ID
    fun getCategoryById(categoryId: Int): Category? {
        return try {
            val categoryByIdResponse = service.getCategoryById(categoryId).execute()
            categoryByIdResponse.body()
        } catch (e: IOException) {
            null
        }
    }

    //category {id} recipes
    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return try {
            val recipesByCategoryId = service.getRecipesByCategoryId(categoryId).execute()
            recipesByCategoryId.body()
        } catch (e: IOException) {
            null
        }
    }

    //category
    fun getCategories(): List<Category>? {
        return try {
            val categoriesResponse: Response<List<Category>> = service.getCategories().execute()
            categoriesResponse.body()
        } catch (e: IOException) {
            null
        }
    }

    companion object {
        private const val BASE_URL = "https://recipes.androidsprint.ru/api/"
    }

}