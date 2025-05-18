package com.example.cobarecipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cobarecipesapp.data.ThreadPoolApp
import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.model.Category
import com.example.cobarecipesapp.utils.ToastHelper


class CategoryListViewModel(application: Application) : AndroidViewModel(application) {

    private var _categoriesState = MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> = _categoriesState

    private val _selectedCategory = MutableLiveData<Category?>()
    val selectedCategory: LiveData<Category?> = _selectedCategory

    val recipesRepository = RecipesRepository()

    fun loadCategories() {
        ThreadPoolApp.threadPool.execute {
            try {
                val categories = recipesRepository.getCategories()
                categories?.let {
                    _categoriesState.postValue(CategoriesState(categories = it))
                } ?: ToastHelper.showToast("Ошибка получения данных")
            } catch (e: Exception) {
                ToastHelper.showToast("Ошибка сети")
            }
        }
    }

    fun loadCategoryById(categoryId: Int) {

        ThreadPoolApp.threadPool.execute {
            try {
                val category = recipesRepository.getCategoryById(categoryId)
                category?.let {
                    _selectedCategory.postValue(category)
                } ?: ToastHelper.showToast("Ошибка получения данных")
            } catch (e: Exception) {
                ToastHelper.showToast("Ошибка сети")
            }
        }
    }

    fun clearNavigation() {
        _selectedCategory.value = null
    }

    data class CategoriesState(
        val categories: List<Category> = emptyList(),
    )
}