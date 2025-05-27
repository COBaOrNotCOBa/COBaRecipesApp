package com.example.cobarecipesapp.data

import com.example.cobarecipesapp.model.Category
import com.example.cobarecipesapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface RecipeApiService {

    @GET("recipe/{recipeId}")
    fun getRecipeById(@Path("recipeId") recipeId: Int): Call<Recipe>

    @GET("category/{categoryId}")
    fun getCategoryById(@Path("categoryId") categoryId: Int): Call<Category>

    @GET("category/{categoryId}/recipes")
    fun getRecipesByCategoryId(@Path("categoryId") categoryId: Int): Call<List<Recipe>>

    @GET("category")
    fun getCategories(): Call<List<Category>>

}