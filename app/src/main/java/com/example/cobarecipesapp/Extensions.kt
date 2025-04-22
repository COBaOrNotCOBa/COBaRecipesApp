package com.example.cobarecipesapp

fun RecipesListAdapter.setOnItemClick(listener: (Int) -> Unit) {
    setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
        override fun onItemClick(recipeId: Int) = listener(recipeId)
    })
}