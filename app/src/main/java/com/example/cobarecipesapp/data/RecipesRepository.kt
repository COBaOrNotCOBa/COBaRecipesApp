package com.example.cobarecipesapp.data

import android.content.Context
import android.util.Log
import com.example.cobarecipesapp.model.Category
import com.example.cobarecipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.IOException


class RecipesRepository(context: Context) {

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

    private val recipesDatabase by lazy { AppDatabase.getDatabase(context) }

    suspend fun getRecipeById(recipeId: Int): Recipe? = withContext(Dispatchers.IO) {
        try {
            val cachedRecipe = recipesDatabase.recipesDao().getRecipeById(recipeId)
            cachedRecipe?.let { return@withContext it }

            val recipe = service.getRecipeById(recipeId).execute().body()
            recipe
        } catch (e: IOException) {
            Log.e("RecipesRepository", "Error when call getRecipeById() ", e)
            null
        }
    }

    suspend fun getRecipesByIds(favoritesId: String): List<Recipe>? = withContext(Dispatchers.IO) {
        try {
            val recipes = service.getRecipesByIds(favoritesId).execute().body()
            recipes
        } catch (e: IOException) {
            Log.e("RecipesRepository", "Error when call getRecipesByIds() ", e)
            null
        }
    }

    suspend fun getCategoryById(categoryId: Int): Category? = withContext(Dispatchers.IO) {
        try {
            val cachedCategory = recipesDatabase.categoriesDao().getCategoryById(categoryId)
            cachedCategory?.let { return@withContext it }

            val categoryById = service.getCategoryById(categoryId).execute().body()
            categoryById
        } catch (e: IOException) {
            Log.e("RecipesRepository", "Error when call getCategoryById() ", e)
            null
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? =
        withContext(Dispatchers.IO) {
            try {
                val cachedRecipesList =
                    recipesDatabase.recipesDao().getRecipesByCategoryId(categoryId)
                if (cachedRecipesList.isNotEmpty()) {
                    return@withContext cachedRecipesList
                }

                val recipesByCategoryId =
                    service.getRecipesByCategoryId(categoryId)
                        .execute()
                        .body()
                        ?.map { it.copy(categoryId = categoryId) }

                recipesByCategoryId?.let {
                    recipesDatabase.recipesDao().insertRecipesList(*it.toTypedArray())
                }
                recipesByCategoryId
            } catch (e: IOException) {
                Log.e("RecipesRepository", "Error when call getRecipesByCategoryId() ", e)
                null
            }
        }

    suspend fun getCategories(): List<Category>? = withContext(Dispatchers.IO) {
        try {
            val cachedCategories = recipesDatabase.categoriesDao().getCategories()
            if (cachedCategories.isNotEmpty()) {
                return@withContext cachedCategories
            }

            val categories = service.getCategories().execute().body()
            categories?.let { recipesDatabase.categoriesDao().insertCategories(*it.toTypedArray()) }
            categories
        } catch (e: IOException) {
            Log.e("RecipesRepository", "Error when call getCategories() ", e)
            null
        }
    }

    fun getFullImageUrl(imageName: String) = "$BASE_IMAGES_URL$imageName"

    companion object {
        private const val BASE_URL = "https://recipes.androidsprint.ru/api/"
        private const val BASE_IMAGES_URL = "${BASE_URL}images/"
    }

}