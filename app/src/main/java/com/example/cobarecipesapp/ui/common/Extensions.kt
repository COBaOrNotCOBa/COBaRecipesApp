package com.example.cobarecipesapp.ui.common

import com.example.cobarecipesapp.ui.recipes.recipeList.RecipesListAdapter

fun RecipesListAdapter.setOnItemClick(listener: (Int) -> Unit) {
    setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
        override fun onItemClick(recipeId: Int) = listener(recipeId)
    })
}