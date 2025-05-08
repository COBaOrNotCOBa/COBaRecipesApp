package com.example.cobarecipesapp.ui.recipes.recipeList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.cobarecipesapp.data.STUB
import com.example.cobarecipesapp.model.Recipe


class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private var _recipesListState = MutableLiveData(RecipesListState())
    val recipesListState = _recipesListState

    fun loadRecipeList(
        categoryId: Int,
        categoryName: String,
        categoryImage: String,
    ) {
        val recipes = STUB.getRecipesByCategoryId(categoryId)
        _recipesListState.value = RecipesListState(
            recipes = recipes,
            categoryName = categoryName,
            categoryImage = getCategoryImage(categoryImage)
        )
    }

    private fun getCategoryImage(imageUrl: String): Drawable? {
        return try {
            Drawable.createFromStream(
                getApplication<Application>().assets.open(imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.e(
                "ImageLoadError",
                "Image not found: ${_recipesListState.value?.categoryName}",
                e
            )
            null
        }
    }

    data class RecipesListState(
        val recipes: List<Recipe> = emptyList(),
        val categoryName: String? = null,
        val categoryImage: Drawable? = null,
    )
}