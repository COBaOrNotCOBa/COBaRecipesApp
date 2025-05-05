package com.example.cobarecipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.cobarecipesapp.data.STUB
import com.example.cobarecipesapp.model.Recipe


class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private var _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState = _favoritesState

    fun loadFavorites() {
        val favoritesId = getFavorites().map { it.toInt() }.toSet()
        val favorites = STUB.getRecipesByIds(favoritesId)
        _favoritesState.value = FavoritesState(favorites)
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