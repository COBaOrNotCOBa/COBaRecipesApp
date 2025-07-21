package com.example.cobarecipesapp.ui.recipes.recipeList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.model.Category
import com.example.cobarecipesapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private var _recipesListState = MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> = _recipesListState

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage
    private var currentCategory: Category? = null

    fun loadRecipeList(categoryId: Int) {
        viewModelScope.launch {
            loadCategory(categoryId)
            showCachedRecipes(categoryId)
            refreshRecipesFromNetwork(categoryId)
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    private suspend fun loadCategory(categoryId: Int) {
        try {
            currentCategory = recipesRepository.getCategoryFromCache(categoryId)
            recipesRepository.fetchCategoryById(categoryId)?.let { networkCategory ->
                recipesRepository.saveCategory(networkCategory)
                currentCategory = networkCategory
                Log.d(TAG, "Категория успешно загружена из сети")
            } ?: Log.e(TAG, "Ошибка сети loadCategory")
            _recipesListState.postValue(createState(emptyList()))
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка загрузки категории", e)
            _toastMessage.postValue("Ошибка загрузки категории")
        }
    }

    private suspend fun showCachedRecipes(categoryId: Int) {
        try {
            val cachedRecipes = recipesRepository.getRecipesByCategoryFromCache(categoryId)
            if (cachedRecipes.isNotEmpty()) {
                _recipesListState.postValue(createState(cachedRecipes))
                Log.d(TAG, "Рецепты успешно загружены из кэша")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка загрузки рецепта showCachedRecipes", e)
            _toastMessage.postValue("Ошибка загрузки рецепта")
        }
    }

    private suspend fun refreshRecipesFromNetwork(categoryId: Int) {
        try {
            val currentRecipes = recipesRepository.getRecipesByCategoryFromCache(categoryId)
            val networkRecipes = recipesRepository.fetchRecipesByCategory(categoryId) ?: run {
                Log.e(TAG, "Ошибка загрузки из сети refreshRecipesFromNetwork")
                _toastMessage.postValue("Ошибка сети")
                if (currentRecipes.isEmpty()) {
                    Log.e(TAG, "Ошибка загрузки из сети и кэша refreshRecipesFromNetwork")
                    _toastMessage.postValue("Не удалось загрузить рецепты")
                }
                return
            }

            Log.d(TAG, "Рецепты успешно загружены из сети")
            val recipesToSave = networkRecipes.map { networkRecipe ->
                currentRecipes.find { it.id == networkRecipe.id }?.let { dbRecipe ->
                    networkRecipe.copy(
                        categoryId = categoryId,
                        isFavorite = dbRecipe.isFavorite
                    )
                } ?: networkRecipe.copy(categoryId = categoryId)
            }

            recipesRepository.saveRecipes(recipesToSave)

            val finalRecipes = recipesRepository.getRecipesByCategoryFromCache(categoryId)
            _recipesListState.postValue(createState(finalRecipes))
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка загрузки refreshRecipesFromNetwork", e)
            _toastMessage.postValue("Ошибка загрузки")
        }
    }

    private fun createState(recipes: List<Recipe>): RecipesListState {
        return RecipesListState(
            recipes = recipes,
            categoryName = currentCategory?.title,
            categoryImageUrl = currentCategory?.imageUrl?.let {
                recipesRepository.getFullImageUrl(it)
            }
        )
    }

    companion object {
        const val TAG = "RecipesListViewModel"
    }

    data class RecipesListState(
        val recipes: List<Recipe> = emptyList(),
        val categoryName: String? = null,
        val categoryImageUrl: String? = null,
    )
}