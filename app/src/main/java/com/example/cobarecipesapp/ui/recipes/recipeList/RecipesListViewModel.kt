package com.example.cobarecipesapp.ui.recipes.recipeList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.model.Category
import com.example.cobarecipesapp.model.Recipe
import kotlinx.coroutines.launch


class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private var _recipesListState = MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> = _recipesListState

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    private val recipesRepository = RecipesRepository(application)
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
            }
            _recipesListState.postValue(createState(emptyList()))
        } catch (e: Exception) {
            Log.e("RecipesListViewModel", "Ошибка загрузки категории", e)
            _toastMessage.postValue("Ошибка загрузки категории")
        }
    }

    private suspend fun showCachedRecipes(categoryId: Int) {
        try {
            val cachedRecipes = recipesRepository.getRecipesByCategoryFromCache(categoryId)
            if (cachedRecipes.isNotEmpty()) {
                _recipesListState.postValue(createState(cachedRecipes))
            }
        } catch (e: Exception) {
            Log.e("RecipesListViewModel", "Ошибка загрузки рецепта", e)
            _toastMessage.postValue("Ошибка загрузки рецепта")
        }
    }

    private suspend fun refreshRecipesFromNetwork(categoryId: Int) {
        try {
            val currentRecipes = recipesRepository.getRecipesByCategoryFromCache(categoryId)
            val networkRecipes = recipesRepository.fetchRecipesByCategory(categoryId) ?: run {
                if (currentRecipes.isEmpty()) {
                    _toastMessage.postValue("Не удалось загрузить рецепты")
                }
                return
            }

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
            Log.e("RecipesListViewModel", "Ошибка сети", e)
            _toastMessage.postValue("Ошибка сети")
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

    data class RecipesListState(
        val recipes: List<Recipe> = emptyList(),
        val categoryName: String? = null,
        val categoryImageUrl: String? = null,
    )
}