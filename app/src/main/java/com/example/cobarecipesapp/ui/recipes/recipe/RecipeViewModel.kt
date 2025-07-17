package com.example.cobarecipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            showCachedRecipe(recipeId)
            refreshRecipeFromNetwork(recipeId)
        }
    }

    fun onFavoritesClicked() {
        recipeState.value?.let { currentState ->
            currentState.recipe?.let { recipe ->
                val newFavoriteStatus = !recipe.isFavorite
                _recipeState.value =
                    (currentState.copy(recipe = recipe.copy(isFavorite = newFavoriteStatus)))

                viewModelScope.launch {
                    recipesRepository.updateFavoriteStatus(recipe.id, newFavoriteStatus)
                }
            }
        }
    }

    fun updatePortionsCount(count: Int) {
        recipeState.value?.let { currentState ->
            _recipeState.value = currentState.copy(portionsCount = count)
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    private suspend fun showCachedRecipe(recipeId: Int) {
        try {
            recipesRepository.getRecipeByIdFromCache(recipeId)?.let { recipe ->
                _recipeState.postValue(createRecipeState(recipe))
            }
        } catch (e: Exception) {
            Log.e("RecipeViewModel", "Error loading cached recipe", e)
        }
    }

    private suspend fun refreshRecipeFromNetwork(recipeId: Int) {
        try {
            val cachedRecipe = recipesRepository.getRecipeByIdFromCache(recipeId)
            val networkRecipe = recipesRepository.fetchRecipeById(recipeId) ?: return

            val currentRecipe = networkRecipe.copy(
                isFavorite = cachedRecipe?.isFavorite ?: false,
                categoryId = cachedRecipe?.categoryId ?: -1
            )

            recipesRepository.saveRecipe(currentRecipe)
            _recipeState.postValue(createRecipeState(currentRecipe))
        } catch (e: Exception) {
            Log.e("RecipeViewModel", "Network error", e)
            if (_recipeState.value?.recipe == null) {
                _toastMessage.postValue("Ошибка сети")
            }
        }
    }

    private fun createRecipeState(recipe: Recipe): RecipeState {
        return RecipeState(
            recipe = recipe,
            portionsCount = _recipeState.value?.portionsCount ?: 1,
            recipeImageUrl = recipesRepository.getFullImageUrl(recipe.imageUrl)
        )
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionsCount: Int = 1,
        val recipeImageUrl: String? = null,
    )
}