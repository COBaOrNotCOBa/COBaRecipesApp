package com.example.cobarecipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cobarecipesapp.model.Recipe
import com.example.cobarecipesapp.data.STUB


class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState

    fun loadRecipe(recipeId: Int) {
        // TODO 'load from network'

        val recipe = STUB.getRecipeById(recipeId)
        val currentState = recipeState.value ?: RecipeState()
        _recipeState.value = currentState.copy(
            recipe = recipe,
            isFavorite = checkIsFavorite(recipeId),
            portionsCount = currentState.portionsCount ?: 1,
        )
    }

    private fun checkIsFavorite(recipeId: Int): Boolean {
        return getFavorites().contains(recipeId.toString())
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = getApplication<Application>().applicationContext.getSharedPreferences(
            FAVORITE_PREFS_KEY, Context.MODE_PRIVATE
        )
        return HashSet(
            sharedPrefs?.getStringSet(FAVORITE_RECIPES_KEY, HashSet())
                ?: mutableSetOf()
        )
    }

    fun onFavoritesClicked() {
        val currentState = recipeState.value ?: RecipeState()
        val currentFavoriteStatus = currentState.isFavorite
        _recipeState.value = currentState.copy(
            isFavorite = !currentFavoriteStatus
        )

        val recipeId = currentState.recipe?.id?.toString() ?: return
        val favorites = getFavorites().apply {
            if (currentFavoriteStatus) remove(recipeId) else add(recipeId)
        }
        saveFavorites(favorites)
    }

    private fun saveFavorites(favoritesId: Set<String>) {
        val sharedPrefs = getApplication<Application>().applicationContext.getSharedPreferences(
            FAVORITE_PREFS_KEY, Context.MODE_PRIVATE
        ) ?: return

        sharedPrefs.edit(commit = true) {
            putStringSet(FAVORITE_RECIPES_KEY, favoritesId)
        }
    }

    init {
        Log.i("!!!", "RecipeViewModel инициализирована")
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val portionsCount: Int? = null,
    )

    companion object {
        const val FAVORITE_RECIPES_KEY = "favorite_recipes_key"
        const val FAVORITE_PREFS_KEY = "favorite_prefs_key"
    }

}