package com.example.cobarecipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cobarecipesapp.data.ThreadPoolApp
import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.model.Recipe
import com.example.cobarecipesapp.utils.ToastHelper


class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState

    val recipesRepository = RecipesRepository()

    fun loadRecipe(recipeId: Int) {
        ThreadPoolApp.threadPool.execute {
            try {
                val recipe = recipesRepository.getRecipeById(recipeId)
                recipe?.let {
                    _recipeState.postValue(
                        RecipeState(
                            recipe = recipe,
                            isFavorite = checkIsFavorite(recipeId),
                            portionsCount = _recipeState.value?.portionsCount ?: 1,
                            recipeImage = getRecipeImage(recipe.imageUrl),
                        )
                    )
                } ?: ToastHelper.showToast("Ошибка получения данных")
            } catch (e: Exception) {
                ToastHelper.showToast("Ошибка сети")
            }
        }
    }

    fun onFavoritesClicked() {
        _recipeState.value?.let { currentState ->
            _recipeState.value = currentState.copy(isFavorite = !currentState.isFavorite)

            currentState.recipe?.id?.toString()?.let { recipeId ->
                val favorites = getFavorites().apply {
                    if (currentState.isFavorite) remove(recipeId) else add(recipeId)
                }
                saveFavorites(favorites)
            }
        }
    }

    fun updatePortionsCount(count: Int) {
        _recipeState.value?.let { currentState ->
            _recipeState.value = currentState.copy(portionsCount = count)
        }
    }

    private fun checkIsFavorite(recipeId: Int): Boolean {
        return getFavorites().contains(recipeId.toString())
    }

    private fun getRecipeImage(imageUrl: String): Drawable? {
        return try {
            Drawable.createFromStream(
                getApplication<Application>().assets.open(imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.e(
                "ImageLoadError",
                "Image not found: ${_recipeState.value?.recipe?.title}",
                e
            )
            null
        }
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

    private fun saveFavorites(favoritesId: Set<String>) {
        val sharedPrefs = getApplication<Application>().applicationContext.getSharedPreferences(
            FAVORITE_PREFS_KEY, Context.MODE_PRIVATE
        ) ?: return

        sharedPrefs.edit { putStringSet(FAVORITE_RECIPES_KEY, favoritesId) }
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val portionsCount: Int = 1,
        val recipeImage: Drawable? = null,
    )

    companion object {
        const val FAVORITE_RECIPES_KEY = "favorite_recipes_key"
        const val FAVORITE_PREFS_KEY = "favorite_prefs_key"
    }
}