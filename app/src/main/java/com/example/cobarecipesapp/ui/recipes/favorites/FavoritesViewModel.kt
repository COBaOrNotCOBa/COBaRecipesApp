package com.example.cobarecipesapp.ui.recipes.favorites

import android.app.Application
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
            try {
                recipesRepository.getFavoriteRecipes()?.let { favorites ->
                    _favoritesState.postValue(FavoritesState(favorites))
                } ?: ToastHelper.showToast("Ошибка получения данных")
            } catch (_: Exception) {
                ToastHelper.showToast("Ошибка сети")
            }
        }
    }

    data class FavoritesState(
        val recipes: List<Recipe> = emptyList(),
    )
}