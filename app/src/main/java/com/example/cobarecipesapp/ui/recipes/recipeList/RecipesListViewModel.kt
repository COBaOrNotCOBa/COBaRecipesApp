package com.example.cobarecipesapp.ui.recipes.recipeList

import android.app.Application
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

    fun loadRecipeList(categoryId: Int) {
        ThreadPoolApp.threadPool.execute {
            try {
                val recipesRepository = RecipesRepository()
                recipesRepository
                    .getCategoryById(categoryId)
                    ?.let { category ->
                        recipesRepository.getRecipesByCategoryId(categoryId)
                            ?.let { recipes ->
                                _recipesListState.postValue(
                                    RecipesListState(
                                        recipes = recipes,
                                        categoryName = category.title,
                                        categoryImageUrl =
                                            recipesRepository.getFullImageUrl(category.imageUrl),
                                    )
                                )
                            }
                            ?: ToastHelper.showToast("Ошибка получения данных")
                    }
                    ?: ToastHelper.showToast("Ошибка получения данных")
            } catch (_: Exception) {
                ToastHelper.showToast("Ошибка сети")
            }
        }
    }

    data class RecipesListState(
        val recipes: List<Recipe> = emptyList(),
        val categoryName: String? = null,
        val categoryImageUrl: String? = null,
    )
}