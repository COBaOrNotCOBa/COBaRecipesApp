package com.example.cobarecipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.cobarecipesapp.model.Category

class CategoryListViewModel(application: Application) : AndroidViewModel(application){

    private var _categories = CategoriesState()
    val categories = _categories


    data class CategoriesState(
        val categories : List<Category> = emptyList()
    )

}