package com.example.cobarecipesapp.data

import com.example.cobarecipesapp.model.Recipe
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipes WHERE categoryId = :categoryId")
    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipeById(recipeId: Int): Recipe?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipesList(vararg recipes: Recipe)
}