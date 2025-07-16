package com.example.cobarecipesapp.di

import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.ui.recipes.recipeList.RecipesListViewModel

class RecipesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<RecipesListViewModel> {


    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
}