package com.example.cobarecipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cobarecipesapp.model.Recipe

class RecipeViewModel : ViewModel() {

    private val _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    data class RecipeState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
    )

    init {
        Log.i("!!!", "RecipeViewModel инициализирована")
        _recipeState.value = _recipeState.value?.copy(isFavorite = true)
    }

}