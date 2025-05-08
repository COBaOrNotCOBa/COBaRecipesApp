package com.example.cobarecipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.cobarecipesapp.data.STUB
import com.example.cobarecipesapp.model.Category

class CategoryListViewModel(application: Application) : AndroidViewModel(application) {

    private var _categoriesState = MutableLiveData(CategoriesState())
    val categoriesState = _categoriesState

    fun loadCategories() {
        val categories = STUB.getCategories()

        _categoriesState.value = CategoriesState(
            categories = categories,
        )
    }

    fun loadCategoryById(categoryId: Int) =
        STUB.getCategories().find { categoryId == it.id }

    data class CategoriesState(
        val categories: List<Category> = emptyList(),
    )
}