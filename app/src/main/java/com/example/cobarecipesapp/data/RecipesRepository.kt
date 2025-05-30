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

    val recipesDatabase by lazy { AppDatabase.getDatabase(context) }

    suspend fun getRecipeById(recipeId: Int): Recipe? = withContext(Dispatchers.IO) {
        try {
            val recipeFromNetwork = service.getRecipeById(recipeId)
                .execute()
                .body()
            recipeFromNetwork?.let {
                val recipeFromDb = recipesDatabase.recipesDao().getRecipeById(it.id)
                recipeFromDb?.let { recipe ->
                    val updatedRecipe = recipe.copy(
                        title = it.title,
                        imageUrl = it.imageUrl,
                        ingredients = it.ingredients,
                        method = it.method,
                    )
                    recipesDatabase.recipesDao().insertRecipe(updatedRecipe)
                    return@withContext updatedRecipe
                } ?: run {
                    recipesDatabase.recipesDao().insertRecipe(it)
                    return@withContext it
                }
            }
            recipesDatabase.recipesDao().getRecipeById(recipeId)
        } catch (e: IOException) {
            Log.e("RecipesRepository", "Error when call getRecipeById() ", e)
            recipesDatabase.recipesDao().getRecipeById(recipeId)
        }
    }

    suspend fun getFavoriteRecipes(): List<Recipe>? =
        withContext(Dispatchers.IO) {
            try {
                val favoritesIdString =
                    recipesDatabase.recipesDao().getFavoriteRecipes().map { it.id }
                        .joinToString(",")
                val recipesFromNetwork = service.getRecipesByIds(favoritesIdString).execute().body()
                recipesFromNetwork?.let {
                    return@withContext it
                }
                recipesDatabase.recipesDao().getFavoriteRecipes()
            } catch (e: IOException) {
                Log.e("RecipesRepository", "Error when call getRecipesByIds() ", e)
                recipesDatabase.recipesDao().getFavoriteRecipes()
            }
        }

    suspend fun getCategoryById(categoryId: Int): Category? = withContext(Dispatchers.IO) {
        try {
            val categoryByIdFromNetwork = service.getCategoryById(categoryId).execute().body()
            categoryByIdFromNetwork?.let {
                recipesDatabase.categoriesDao().updateCategory(it)
                return@withContext it
            }
            recipesDatabase.categoriesDao().getCategoryById(categoryId)
        } catch (e: IOException) {
            Log.e("RecipesRepository", "Error when call getCategoryById() ", e)
            recipesDatabase.categoriesDao().getCategoryById(categoryId)
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? =
        withContext(Dispatchers.IO) {
            try {
                val recipesFromNetwork =
                    service.getRecipesByCategoryId(categoryId)
                        .execute()
                        .body()
                        ?.map { it.copy(categoryId = categoryId) }

                recipesFromNetwork?.forEach { networkRecipe ->
                    recipesDatabase.recipesDao().getRecipeById(networkRecipe.id)
                        ?.let { localRecipe ->
                            val updatedRecipe = localRecipe.copy(
                                title = networkRecipe.title,
                                ingredients = networkRecipe.ingredients,
                                method = networkRecipe.method,
                                imageUrl = networkRecipe.imageUrl,
                                categoryId = categoryId
                            )
                            recipesDatabase.recipesDao().insertRecipesList(updatedRecipe)
                        } ?: run {
                        recipesDatabase.recipesDao().insertRecipesList(networkRecipe)
                    }
                }
                return@withContext recipesDatabase.recipesDao().getRecipesByCategoryId(categoryId)
            } catch (e: IOException) {
                Log.e("RecipesRepository", "Error when call getRecipesByCategoryId() ", e)
                recipesDatabase.recipesDao().getRecipesByCategoryId(categoryId)
            }
        }

    suspend fun getCategories(): List<Category>? = withContext(Dispatchers.IO) {
        try {
            val categoriesFromNetwork = service.getCategories().execute().body()
            categoriesFromNetwork?.let {
                recipesDatabase.categoriesDao().insertCategories(*it.toTypedArray())
                return@withContext it
            }
            recipesDatabase.categoriesDao().getCategories()
        } catch (e: IOException) {
            Log.e("RecipesRepository", "Error when call getCategories() ", e)
            recipesDatabase.categoriesDao().getCategories()
        }
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