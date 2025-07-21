package com.example.cobarecipesapp.ui.recipes.favorites

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
class FavoritesViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private var _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> = _favoritesState

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    fun loadFavorites() {
        viewModelScope.launch {
            showCachedFavorites()
            refreshFavoritesFromNetwork()
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    private suspend fun showCachedFavorites() {
        try {
            val cachedFavorites = recipesRepository.getFavoriteRecipesFromCache()
            if (cachedFavorites.isNotEmpty()) {
                _favoritesState.postValue(FavoritesState(recipes = cachedFavorites))
                Log.d(TAG, "Избранные успешно загруженны из кэша")
            } else {
                _favoritesState.postValue(FavoritesState(recipes = emptyList()))
                Log.d(TAG, "Список избранных пуст")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка загрузки из кэша", e)
            _favoritesState.postValue(FavoritesState(recipes = emptyList()))
        }
    }

    private suspend fun refreshFavoritesFromNetwork() {
        try {
            val currentFavorites = recipesRepository.getFavoriteRecipesFromCache()
            if (currentFavorites.isEmpty()) return

            val favoriteIds = recipesRepository.getFavoriteListFromCache()
            val networkFavorites = recipesRepository.fetchFavoriteRecipes(favoriteIds)

            if (networkFavorites == null) {
                Log.e(TAG, "Ошибка сети refreshFavoritesFromNetwork")
                _toastMessage.postValue("Ошибка сети")
                return
            }

            Log.d(TAG, "Избранные успешно получиенны из сети")
            val updatedRecipes = currentFavorites.mapNotNull { cachedRecipe ->
                networkFavorites.find { it.id == cachedRecipe.id }?.copy(
                    isFavorite = cachedRecipe.isFavorite,
                    categoryId = cachedRecipe.categoryId
                )
            }

            recipesRepository.saveRecipes(updatedRecipes)
            _favoritesState.postValue(FavoritesState(recipes = updatedRecipes))
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка загрузки refreshFavoritesFromNetwork", e)
            _toastMessage.postValue("Ошибка загрузки")
        }
    }

    private companion object {
        const val TAG = "FavoritesViewModel"
    }

    data class FavoritesState(
        val recipes: List<Recipe> = emptyList(),
    )
}