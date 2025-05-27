package com.example.cobarecipesapp.ui.recipes.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.model.Recipe
import com.example.cobarecipesapp.utils.ToastHelper
import kotlinx.coroutines.launch


class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState

    private val recipesRepository = RecipesRepository(application)

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            try {
                recipesRepository.getRecipeById(recipeId)?.let { recipe ->
                    _recipeState.postValue(
                        RecipeState(
                            recipe = recipe,
                            portionsCount = _recipeState.value?.portionsCount ?: 1,
                            recipeImageUrl = recipesRepository.getFullImageUrl(recipe.imageUrl),
                        )
                    )
                } ?: ToastHelper.showToast("Ошибка получения данных")
            } catch (_: Exception) {
                ToastHelper.showToast("Ошибка сети")
            }
        }
    }

    fun onFavoritesClicked() {
        _recipeState.value?.let { currentState ->
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
        _recipeState.value?.let { currentState ->
            _recipeState.value = currentState.copy(portionsCount = count)
        }
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val portionsCount: Int = 1,
        val recipeImageUrl: String? = null,
    )

}