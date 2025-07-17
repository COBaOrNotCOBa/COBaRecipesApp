package com.example.cobarecipesapp.di

import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<RecipeViewModel> {

    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}