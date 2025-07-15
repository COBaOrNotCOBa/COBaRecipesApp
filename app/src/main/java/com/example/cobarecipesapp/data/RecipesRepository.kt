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
import kotlin.collections.map


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


    suspend fun fetchRecipeById(recipeId: Int): Recipe? = withContext(Dispatchers.IO) {
        try {
            service.getRecipeById(recipeId).execute().body()
        } catch (e: IOException) {
            Log.e("RecipesRepository", "Network error in fetchRecipeById()", e)
            null
        }
    }

    suspend fun fetchRecipesByCategory(categoryId: Int): List<Recipe>? =
        withContext(Dispatchers.IO) {
            try {
                service.getRecipesByCategoryId(categoryId).execute().body()
            } catch (e: IOException) {
                Log.e("RecipesRepository", "Network error in fetchRecipesByCategory()", e)
                null
            }
        }

    suspend fun fetchFavoriteRecipes(favoriteIds: String): List<Recipe>? =
        withContext(Dispatchers.IO) {
            try {
                service.getRecipesByIds(favoriteIds).execute().body()
            } catch (e: IOException) {
                Log.e("RecipesRepository", "Network error in fetchFavoriteRecipes()", e)
                null
            }
        }

    suspend fun fetchCategoryById(categoryId: Int): Category? =
        withContext(Dispatchers.IO) {
            try {
                service.getCategoryById(categoryId).execute().body()
            } catch (e: IOException) {
                Log.e("RecipesRepository", "Network error in fetchCategoryById()", e)
                null
            }
        }

    suspend fun fetchAllCategories(): List<Category>? =
        withContext(Dispatchers.IO) {
            try {
                service.getCategories().execute().body()
            } catch (e: IOException) {
                Log.e("RecipesRepository", "Network error in fetchAllCategories()", e)
                null
            }
        }


    suspend fun getRecipeByIdFromCache(recipeId: Int): Recipe? =
        withContext(Dispatchers.IO) {
            recipesDatabase.recipesDao().getRecipeById(recipeId)
        }

    suspend fun getRecipesByCategoryFromCache(categoryId: Int): List<Recipe> =
        withContext(Dispatchers.IO) {
            recipesDatabase.recipesDao().getRecipesByCategoryId(categoryId)
        }

    suspend fun getFavoriteRecipesFromCache(): List<Recipe> =
        withContext(Dispatchers.IO) {
            recipesDatabase.recipesDao().getFavoriteRecipes()
        }

    suspend fun getFavoriteListFromCache(): String =
        withContext(Dispatchers.IO) {
            recipesDatabase.recipesDao().getFavoriteRecipes().map { it.id }
                .joinToString(",")
        }

    suspend fun getCategoryFromCache(categoryId: Int): Category? =
        withContext(Dispatchers.IO) {
            recipesDatabase.categoriesDao().getCategoryById(categoryId)
        }

    suspend fun getAllCategoriesFromCache(): List<Category> =
        withContext(Dispatchers.IO) {
            recipesDatabase.categoriesDao().getCategories()
        }


    suspend fun saveRecipe(recipe: Recipe) =
        withContext(Dispatchers.IO) {
            recipesDatabase.recipesDao().insertRecipe(recipe)
        }

    suspend fun saveRecipes(recipes: List<Recipe>) =
        withContext(Dispatchers.IO) {
            recipesDatabase.recipesDao().insertRecipesList(*recipes.toTypedArray())
        }

    suspend fun saveCategory(category: Category) =
        withContext(Dispatchers.IO) {
            recipesDatabase.categoriesDao().insertCategories(category)
        }

    suspend fun saveCategories(categories: List<Category>) =
        withContext(Dispatchers.IO) {
            recipesDatabase.categoriesDao().insertCategories(*categories.toTypedArray())
        }

    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean) =
        withContext(Dispatchers.IO) {
            recipesDatabase.recipesDao().updateFavoriteStatus(recipeId, isFavorite)
        }

    fun getFullImageUrl(imageName: String) = "$BASE_IMAGES_URL$imageName"

    companion object {
        private const val BASE_URL = "https://recipes.androidsprint.ru/api/"
        private const val BASE_IMAGES_URL = "${BASE_URL}images/"
    }
}