package com.example.cobarecipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
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
                val favoritesId = getFavorites().joinToString(",")
                recipesRepository.getRecipesByIds(favoritesId)?.let { favorites ->
                    _favoritesState.postValue(FavoritesState(favorites))
                } ?: ToastHelper.showToast("Ошибка получения данных")
            } catch (_: Exception) {
                ToastHelper.showToast("Ошибка сети")
            }
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = getApplication<Application>().applicationContext.getSharedPreferences(
            FavoritesFragment.FAVORITE_PREFS_KEY, Context.MODE_PRIVATE
        )
        return HashSet(
            sharedPrefs?.getStringSet(FavoritesFragment.FAVORITE_RECIPES_KEY, HashSet())
                ?: mutableSetOf()
        )
    }

    data class FavoritesState(
        val recipes: List<Recipe> = emptyList(),
    )
}