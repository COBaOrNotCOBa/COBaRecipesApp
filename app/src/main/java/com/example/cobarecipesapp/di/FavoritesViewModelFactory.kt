package com.example.cobarecipesapp.di

import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.ui.recipes.favorites.FavoritesViewModel

class FavoritesViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<FavoritesViewModel> {

    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }
}