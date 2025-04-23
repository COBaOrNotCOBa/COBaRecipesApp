package com.example.cobarecipesapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.cobarecipesapp.model.Recipe


class RecipeViewModel : ViewModel() {

    data class RecipeState(
        val recipe: Recipe? = null
    )

}