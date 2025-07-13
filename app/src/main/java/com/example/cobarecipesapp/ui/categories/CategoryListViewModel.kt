package com.example.cobarecipesapp.ui.categories

import android.app.Application
import android.util.Log
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

    private val recipesRepository = RecipesRepository(application)

    fun loadCategories() {
        viewModelScope.launch {
            showCachedCategories()
            refreshCategoriesFromNetwork()
        }
    }

    fun loadCategoryById(categoryId: Int) {
        viewModelScope.launch {
            try {
                recipesRepository.getCategoryFromCache(categoryId)?.let { category ->
                    _selectedCategory.postValue(category)
                }

                recipesRepository.fetchCategoryById(categoryId)?.let { networkCategory ->
                    recipesRepository.saveCategory(networkCategory)
                    _selectedCategory.postValue(networkCategory)
                }
            } catch (e: Exception) {
                Log.e("CategoryListViewModel", "Ошибка загрузки категории", e)
                ToastHelper.showToast("Ошибка загрузки категории")
            }
        }
    }

    fun clearNavigation() {
        _selectedCategory.value = null
    }

    private suspend fun showCachedCategories() {
        try {
            val cachedCategories = recipesRepository.getAllCategoriesFromCache()
            if (cachedCategories.isNotEmpty()) {
                _categoriesState.postValue(CategoriesState(categories = cachedCategories))
            }
        } catch (e: Exception) {
            Log.e("CategoryListViewModel", "Ошибка загрузки категорий из кэша", e)
            ToastHelper.showToast("Ошибка загрузки категорий")
        }
    }

    private suspend fun refreshCategoriesFromNetwork() {
        try {
            recipesRepository.fetchAllCategories()?.let { networkCategories ->
                recipesRepository.saveCategories(networkCategories)
                _categoriesState.postValue(CategoriesState(categories = networkCategories))
            } ?: ToastHelper.showToast("Ошибка сети")
        } catch (e: Exception) {
            Log.e("CategoryListViewModel", "Ошибка загрузки категорий из сети", e)
            ToastHelper.showToast("Ошибка сети")
        }
    }

    data class CategoriesState(
        val categories: List<Category> = emptyList(),
    )
}