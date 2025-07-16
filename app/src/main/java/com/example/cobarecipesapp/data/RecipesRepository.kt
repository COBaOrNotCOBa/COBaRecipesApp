package com.example.cobarecipesapp.data

import android.util.Log
import com.example.cobarecipesapp.model.Category
import com.example.cobarecipesapp.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.collections.map


class RecipesRepository(
    private val recipesDao: RecipesDao,
    private val categoriesDao: CategoriesDao,
    private val recipeApiService: RecipeApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun fetchRecipeById(recipeId: Int): Recipe? = withContext(ioDispatcher) {
        try {
            recipeApiService.getRecipeById(recipeId).execute().body()
        } catch (e: IOException) {
            Log.e("RecipesRepository", "Network error in fetchRecipeById()", e)
            null
        }
    }

    suspend fun fetchRecipesByCategory(categoryId: Int): List<Recipe>? =
        withContext(ioDispatcher) {
            try {
                recipeApiService.getRecipesByCategoryId(categoryId).execute().body()
            } catch (e: IOException) {
                Log.e("RecipesRepository", "Network error in fetchRecipesByCategory()", e)
                null
            }
        }

    suspend fun fetchFavoriteRecipes(favoriteIds: String): List<Recipe>? =
        withContext(ioDispatcher) {
            try {
                recipeApiService.getRecipesByIds(favoriteIds).execute().body()
            } catch (e: IOException) {
                Log.e("RecipesRepository", "Network error in fetchFavoriteRecipes()", e)
                null
            }
        }

    suspend fun fetchCategoryById(categoryId: Int): Category? =
        withContext(ioDispatcher) {
            try {
                recipeApiService.getCategoryById(categoryId).execute().body()
            } catch (e: IOException) {
                Log.e("RecipesRepository", "Network error in fetchCategoryById()", e)
                null
            }
        }

    suspend fun fetchAllCategories(): List<Category>? =
        withContext(ioDispatcher) {
            try {
                recipeApiService.getCategories().execute().body()
            } catch (e: IOException) {
                Log.e("RecipesRepository", "Network error in fetchAllCategories()", e)
                null
            }
        }


    suspend fun getRecipeByIdFromCache(recipeId: Int): Recipe? =
        withContext(ioDispatcher) {
            recipesDao.getRecipeById(recipeId)
        }

    suspend fun getRecipesByCategoryFromCache(categoryId: Int): List<Recipe> =
        withContext(ioDispatcher) {
            recipesDao.getRecipesByCategoryId(categoryId)
        }

    suspend fun getFavoriteRecipesFromCache(): List<Recipe> =
        withContext(ioDispatcher) {
            recipesDao.getFavoriteRecipes()
        }

    suspend fun getFavoriteListFromCache(): String =
        withContext(ioDispatcher) {
            recipesDao.getFavoriteRecipes().map { it.id }
                .joinToString(",")
        }

    suspend fun getCategoryFromCache(categoryId: Int): Category? =
        withContext(ioDispatcher) {
            categoriesDao.getCategoryById(categoryId)
        }

    suspend fun getAllCategoriesFromCache(): List<Category> =
        withContext(ioDispatcher) {
            categoriesDao.getCategories()
        }


    suspend fun saveRecipe(recipe: Recipe) =
        withContext(ioDispatcher) {
            recipesDao.insertRecipe(recipe)
        }

    suspend fun saveRecipes(recipes: List<Recipe>) =
        withContext(ioDispatcher) {
            recipesDao.insertRecipesList(*recipes.toTypedArray())
        }

    suspend fun saveCategory(category: Category) =
        withContext(ioDispatcher) {
            categoriesDao.insertCategories(category)
        }

    suspend fun saveCategories(categories: List<Category>) =
        withContext(ioDispatcher) {
            categoriesDao.insertCategories(*categories.toTypedArray())
        }

    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean) =
        withContext(ioDispatcher) {
            recipesDao.updateFavoriteStatus(recipeId, isFavorite)
        }

    fun getFullImageUrl(imageName: String) = "$BASE_IMAGES_URL$imageName"

    companion object {
        const val BASE_URL = "https://recipes.androidsprint.ru/api/"
        private const val BASE_IMAGES_URL = "${BASE_URL}images/"
    }
}