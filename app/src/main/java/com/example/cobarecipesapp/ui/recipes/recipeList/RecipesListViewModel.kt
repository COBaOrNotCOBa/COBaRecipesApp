package com.example.cobarecipesapp.ui.recipes.recipeList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cobarecipesapp.data.ThreadPoolApp
import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.model.Recipe
import com.example.cobarecipesapp.utils.ToastHelper


class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    private var _recipesListState = MutableLiveData(RecipesListState())
    val recipesListState: LiveData<RecipesListState> = _recipesListState

    val recipesRepository = RecipesRepository()

    fun loadRecipeList(categoryId: Int) {
        ThreadPoolApp.threadPool.execute {
            try {
                val category = recipesRepository.getCategoryById(categoryId)
                val recipes = recipesRepository.getRecipesByCategoryId(categoryId)

                category?.let { category ->
                    recipes?.let { recipes ->
                        _recipesListState.postValue(
                            RecipesListState(
                                recipes = recipes,
                                categoryName = category.title,
                                categoryImage = getCategoryImage(category.imageUrl)
                            )
                        )
                    } ?: ToastHelper.showToast("Ошибка получения данных")
                } ?: ToastHelper.showToast("Ошибка получения данных")
            } catch (e: Exception) {
                ToastHelper.showToast("Ошибка сети")
            }
        }
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