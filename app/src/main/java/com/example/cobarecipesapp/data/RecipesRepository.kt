package com.example.cobarecipesapp.data

import android.content.Context
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

    private val recipesDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "recipes-db"
    ).build()

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
//                val cachedRecipesList = getRecipesListFromCache(categoryId)
//                if (cachedRecipesList.isNotEmpty()) {
//                    return@withContext cachedRecipesList
//                }

                val recipesByCategoryId =
                    service.getRecipesByCategoryId(categoryId).execute().body()
                recipesByCategoryId?.let { saveRecipesListInCache(it) }
                recipesByCategoryId
//                recipesByCategoryId?.map { recipe ->
//                    recipe.copy(categoryId = categoryId)
//                }?.let { recipesWithCategoryId ->
//                    saveRecipesListInCache(recipesWithCategoryId)
//                    recipesWithCategoryId
//                }
            } catch (_: IOException) {
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
        } catch (_: IOException) {
            null
        }
    }

    fun getFullImageUrl(imageName: String) = "$BASE_IMAGES_URL$imageName"

    private suspend fun getCategoriesFromCache() = withContext(Dispatchers.IO) {
        dbRecipes.categoriesDao().getCategories()
    }

    private suspend fun saveCategoriesInCache(categories: List<Category>) =
        withContext(Dispatchers.IO) {
            dbRecipes.categoriesDao().insertCategories(*categories.toTypedArray())
        }


//    private suspend fun getRecipesListFromCache(categoryId: Int) = withContext(Dispatchers.IO) {
//        dbRecipes.recipesDao().getRecipesByCategoryId(categoryId)
//    }

    private suspend fun saveRecipesListInCache(recipesList: List<Recipe>) =
        withContext(Dispatchers.IO) {
            dbRecipes.recipesDao().insertRecipesList(*recipesList.toTypedArray())
        }

    companion object {
        private const val BASE_URL = "https://recipes.androidsprint.ru/api/"
        private const val BASE_IMAGES_URL = "${BASE_URL}images/"
    }

}