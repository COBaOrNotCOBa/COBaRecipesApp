package com.example.cobarecipesapp.ui.recipes.favorites

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.model.Recipe
import com.example.cobarecipesapp.utils.ToastHelper
import kotlinx.coroutines.launch


class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private var _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> = _favoritesState

    private val recipesRepository = RecipesRepository(application)

    fun loadFavorites() {
        viewModelScope.launch {
            showCachedFavorites()
            refreshFavoritesFromNetwork()
        }
    }

    private suspend fun showCachedFavorites() {
        try {
            val cachedFavorites = recipesRepository.getFavoriteRecipesFromCache()
            if (cachedFavorites.isNotEmpty()) {
                _favoritesState.postValue(FavoritesState(recipes = cachedFavorites))
            } else {
                _favoritesState.postValue(FavoritesState(recipes = emptyList()))
            }
        } catch (e: Exception) {
            Log.e("FavoritesViewModel", "Ошибка загрузки из кэша", e)
            _favoritesState.postValue(FavoritesState(recipes = emptyList()))
        }
    }

    private suspend fun refreshFavoritesFromNetwork() {
        try {
            val currentFavorites = recipesRepository.getFavoriteRecipesFromCache()
            if (currentFavorites.isEmpty()) return

            val favoriteIds = recipesRepository.getFavoriteListFromCache()
            val networkFavorites = recipesRepository.fetchFavoriteRecipes(favoriteIds) ?: return

            val updatedRecipes = currentFavorites.mapNotNull { cachedRecipe ->
                networkFavorites.find { it.id == cachedRecipe.id }?.copy(
                    isFavorite = cachedRecipe.isFavorite,
                    categoryId = cachedRecipe.categoryId
                )
            }

            recipesRepository.saveRecipes(updatedRecipes)
            _favoritesState.postValue(FavoritesState(recipes = updatedRecipes))
        } catch (e: Exception) {
            Log.e("FavoritesViewModel", "Ошибка сети", e)
            ToastHelper.showToast("Ошибка сети")
        }
    }

    data class FavoritesState(
        val recipes: List<Recipe> = emptyList(),
    )
}