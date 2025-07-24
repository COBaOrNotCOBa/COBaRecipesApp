package com.example.cobarecipesapp.data

import android.util.Log
import com.example.cobarecipesapp.di.IoDispatcher
import com.example.cobarecipesapp.model.Category
import com.example.cobarecipesapp.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.collections.map


class RecipesRepository @Inject constructor(
    private val recipesDao: RecipesDao,
    private val categoriesDao: CategoriesDao,
    private val recipeApiService: RecipeApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun fetchRecipeById(recipeId: Int): Recipe? =
        withContext(ioDispatcher) {
            try {
                val response = recipeApiService.getRecipeById(recipeId).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    throw IOException("Ошибка сервера: ${response.code()} - ${response.message()}")
                }
            } catch (e: IOException) {
                Log.e(TAG, "Сетевая ошибка в fetchRecipeById()", e)
                null
            } catch (e: Exception) {
                Log.e(TAG, "Неожиданная ошибка в fetchRecipeById()", e)
                null
            }
        }

    suspend fun fetchRecipesByCategory(categoryId: Int): List<Recipe>? =
        withContext(ioDispatcher) {
            try {
                val response = recipeApiService.getRecipesByCategoryId(categoryId).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    throw IOException("Ошибка сервера: ${response.code()} - ${response.message()}")
                }
            } catch (e: IOException) {
                Log.e(TAG, "Сетевая ошибка в fetchRecipesByCategory()", e)
                null
            } catch (e: Exception) {
                Log.e(TAG, "Неожиданная ошибка в fetchRecipesByCategory()", e)
                null
            }
        }

    suspend fun fetchFavoriteRecipes(favoriteIds: String): List<Recipe>? =
        withContext(ioDispatcher) {
            try {
                val response = recipeApiService.getRecipesByIds(favoriteIds).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    throw IOException("Ошибка сервера: ${response.code()} - ${response.message()}")
                }
            } catch (e: IOException) {
                Log.e(TAG, "Сетевая ошибка в fetchFavoriteRecipes()", e)
                null
            } catch (e: Exception) {
                Log.e(TAG, "Неожиданная ошибка в fetchFavoriteRecipes()", e)
                null
            }
        }

    suspend fun fetchCategoryById(categoryId: Int): Category? =
        withContext(ioDispatcher) {
            try {
                val response = recipeApiService.getCategoryById(categoryId).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    throw IOException("Ошибка сервера: ${response.code()} - ${response.message()}")
                }
            } catch (e: IOException) {
                Log.e(TAG, "Сетевая ошибка в fetchCategoryById()", e)
                null
            } catch (e: Exception) {
                Log.e(TAG, "Неожиданная ошибка в fetchCategoryById()", e)
                null
            }
        }

    suspend fun fetchAllCategories(): List<Category>? =
        withContext(ioDispatcher) {
            Log.d("DISPATCHER", "Current thread: ${Thread.currentThread().name}")
            try {
                val response = recipeApiService.getCategories().execute()
                if (response.isSuccessful) {
                    response.body().also {
                        Log.d(TAG, "Успешно загружено ${it?.size ?: 0} категорий")
                    }
                } else {
                    Log.e(TAG, "Ошибка сервера: ${response.code()} - ${response.message()}")
                    null
                }
            } catch (e: IOException) {
                Log.e(TAG, "Сетевая ошибка в fetchAllCategories()", e)
                null
            } catch (e: Exception) {
                Log.e(TAG, "Неожиданная ошибка в fetchAllCategories()", e)
                null
            }
        }


    suspend fun getRecipeByIdFromCache(recipeId: Int): Recipe? =
        withContext(ioDispatcher) {
            try {
                recipesDao.getRecipeById(recipeId).also {
                    if (it == null) {
                        Log.d(TAG, "Рецепт с ID $recipeId не найден в кэше")
                    } else {
                        Log.v(TAG, "Успешно загружен рецепт из кэша: ${it.title}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при загрузке рецепта $recipeId из кэша", e)
                null
            }
        }

    suspend fun getRecipesByCategoryFromCache(categoryId: Int): List<Recipe> =
        withContext(ioDispatcher) {
            try {
                recipesDao.getRecipesByCategoryId(categoryId).also {
                    Log.d(TAG, "Загружено ${it.size} рецептов категории $categoryId из кэша")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при загрузке рецептов категории $categoryId из кэша", e)
                emptyList()
            }
        }

    suspend fun getFavoriteRecipesFromCache(): List<Recipe> =
        withContext(ioDispatcher) {
            try {
                recipesDao.getFavoriteRecipes().also {
                    Log.d(TAG, "Загружено ${it.size} избранных рецептов из кэша")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при загрузке избранных рецептов из кэша", e)
                emptyList()
            }
        }

    suspend fun getFavoriteListFromCache(): String =
        withContext(ioDispatcher) {
            try {
                recipesDao.getFavoriteRecipes().map { it.id }
                    .joinToString(",")
                    .also {
                        Log.d(TAG, "Загружен список избранных ID: $it")
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при загрузке списка избранных", e)
                ""
            }
        }

    suspend fun getCategoryFromCache(categoryId: Int): Category? =
        withContext(ioDispatcher) {
            try {
                categoriesDao.getCategoryById(categoryId).also {
                    if (it == null) {
                        Log.d(TAG, "Категория с ID $categoryId не найдена в кэше")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при загрузке категории $categoryId из кэша", e)
                null
            }
        }

    suspend fun getAllCategoriesFromCache(): List<Category> =
        withContext(ioDispatcher) {
            try {
                categoriesDao.getCategories().also {
                    Log.d(TAG, "Загружено ${it.size} категорий из кэша")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при загрузке всех категорий из кэша", e)
                emptyList()
            }
        }


    suspend fun saveRecipe(recipe: Recipe) =
        withContext(ioDispatcher) {
            try {
                recipesDao.insertRecipe(recipe)
                Log.d(TAG, "Успешно сохранен $recipe рецепт")
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка сохранения рецепта", e)
                throw e
            }
        }

    suspend fun saveRecipes(recipes: List<Recipe>) =
        withContext(ioDispatcher) {
            try {
                recipesDao.insertRecipesList(*recipes.toTypedArray())
                Log.d(TAG, "Успешно сохранены $recipes рецепты")
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка сохранения рецептов", e)
                throw e
            }
        }

    suspend fun saveCategory(category: Category) =
        withContext(ioDispatcher) {
            try {
                categoriesDao.insertCategories(category)
                Log.d(TAG, "Успешно сохранена $category категория")
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка сохранения категории", e)
                throw e
            }
        }

    suspend fun saveCategories(categories: List<Category>) =
        withContext(ioDispatcher) {
            try {
                categoriesDao.insertCategories(*categories.toTypedArray())
                Log.d(TAG, "Успешно сохранено ${categories.size} категорий")
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка сохранения категорий", e)
                throw e
            }
        }

    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean) =
        withContext(ioDispatcher) {
            try {
                recipesDao.updateFavoriteStatus(recipeId, isFavorite)
                Log.d(
                    TAG,
                    "Успешно обновлён статус избарнного рецепта с ID $recipeId на $isFavorite"
                )
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка обновления статуса избанного рецепта с ID $recipeId")
                throw e
            }
        }

    fun getFullImageUrl(imageName: String) = "$BASE_IMAGES_URL$imageName"

    companion object {
        const val BASE_URL = "https://recipes.androidsprint.ru/api/"
        const val BASE_IMAGES_URL = "${BASE_URL}images/"
        const val TAG = "RecipesRepository"
    }
}