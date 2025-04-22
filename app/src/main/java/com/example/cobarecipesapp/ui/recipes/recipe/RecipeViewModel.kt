package com.example.cobarecipesapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.cobarecipesapp.model.Ingredient


data class RecipeState(
    var title: String? = null,
    var ingredients: List<Ingredient> = emptyList(),
    var method: List<String> = emptyList(),
    var imageUrl: String? = null,
)

class RecipeViewModel : ViewModel()