package com.example.cobarecipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.model.Category
import com.example.cobarecipesapp.utils.ToastHelper
import kotlinx.coroutines.launch


class CategoryListViewModel(application: Application) : AndroidViewModel(application) {

    private var _categoriesState = MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> = _categoriesState

    private val _selectedCategory = MutableLiveData<Category?>()
    val selectedCategory: LiveData<Category?> = _selectedCategory

    private val recipesRepository = RecipesRepository()

    fun loadCategories() {
        try {
            viewModelScope.launch {
                recipesRepository.getCategories()?.let { categories ->
                    _categoriesState.postValue(CategoriesState(categories = categories))
                } ?: ToastHelper.showToast("Ошибка получения данных")
            }
        } catch (_: Exception) {
            ToastHelper.showToast("Ошибка сети")
        }
    }

    fun loadCategoryById(categoryId: Int) {
        try {
            viewModelScope.launch {
                recipesRepository.getCategoryById(categoryId)?.let { category ->
                    _selectedCategory.postValue(category)
                } ?: ToastHelper.showToast("Ошибка получения данных")
            }
        } catch (_: Exception) {
            ToastHelper.showToast("Ошибка сети")
        }
    }

    fun clearNavigation() {
        _selectedCategory.value = null
    }

    data class CategoriesState(
        val categories: List<Category> = emptyList(),
    )
}