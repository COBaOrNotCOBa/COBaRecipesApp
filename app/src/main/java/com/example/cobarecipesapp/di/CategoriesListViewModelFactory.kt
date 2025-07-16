package com.example.cobarecipesapp.di

import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.ui.categories.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<CategoriesListViewModel> {

    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }
}