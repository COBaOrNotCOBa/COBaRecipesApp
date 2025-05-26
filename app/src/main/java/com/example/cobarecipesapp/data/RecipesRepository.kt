package com.example.cobarecipesapp.data

import android.content.Context
import android.util.Log
import androidx.room.Room
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

    private val recipesDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "recipes-db"
        ).build()
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? = withContext(Dispatchers.IO) {
        try {
            val recipe = service.getRecipeById(recipeId).execute().body()
            recipe
        } catch (_: IOException) {
            null
        }
    }

    suspend fun getRecipesByIds(favoritesId: String): List<Recipe>? = withContext(Dispatchers.IO) {
        try {
            val recipes = service.getRecipesByIds(favoritesId).execute().body()
            recipes
        } catch (_: IOException) {
            null
        }
    }

    suspend fun getCategoryById(categoryId: Int): Category? = withContext(Dispatchers.IO) {
        try {
            val categoryById = service.getCategoryById(categoryId).execute().body()
            categoryById
        } catch (_: IOException) {
            null
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? =
        withContext(Dispatchers.IO) {
            try {
                Log.d("RECIPE_LOAD", "Checking cache for category $categoryId")
                val cachedRecipesList =
                    recipesDatabase.recipesDao().getRecipesByCategoryId(categoryId)
                if (cachedRecipesList.isNotEmpty()) {
                    Log.d("RECIPE_LOAD", "Returning ${cachedRecipesList.size} cached recipes")
                    return@withContext cachedRecipesList
                }

                Log.d("RECIPE_LOAD", "Fetching from network for category $categoryId")
                val response = service.getRecipesByCategoryId(categoryId).execute()
                Log.d("API_RESPONSE", "Code: ${response.code()}, Body: ${response.body()}")
                if (!response.isSuccessful) {
                    Log.e("RECIPE_LOAD", "Network error: ${response.code()}")
                    return@withContext null
                }

                val recipesByCategoryId =
                    service.getRecipesByCategoryId(categoryId)
                        .execute()
                        .body()
                        ?.map { it.copy(categoryId = categoryId) }

                if (recipesByCategoryId.isNullOrEmpty()) {
                    Log.w("RECIPE_LOAD", "Empty recipes list from API")
                    return@withContext null
                }

                Log.d("RECIPE_LOAD", "Saving ${recipesByCategoryId.size} recipes to DB")
                recipesByCategoryId?.let {
                    recipesDatabase.recipesDao().insertRecipesList(*it.toTypedArray())
                }
                recipesByCategoryId
            } catch (_: IOException) {
                null
            }
        }

    suspend fun getCategories(): List<Category>? = withContext(Dispatchers.IO) {
        try {
            Log.d("DB_CHECK", "Is DB open: ${recipesDatabase.isOpen}")

            val cachedCategories = recipesDatabase.categoriesDao().getCategories()
            Log.d("CACHE_CHECK", "Cached categories count: ${cachedCategories.size}")
            if (cachedCategories.isNotEmpty()) {
                return@withContext cachedCategories
            }

            val categories = service.getCategories().execute().body()
            Log.d("NETWORK_DATA", "Received categories: ${categories?.size ?: "null"}")
            categories?.let { recipesDatabase.categoriesDao().insertCategories(*it.toTypedArray()) }
            categories
        } catch (_: IOException) {
            null
        }
    }

    fun getFullImageUrl(imageName: String) = "$BASE_IMAGES_URL$imageName"

    companion object {
        private const val BASE_URL = "https://recipes.androidsprint.ru/api/"
        private const val BASE_IMAGES_URL = "${BASE_URL}images/"
    }

}