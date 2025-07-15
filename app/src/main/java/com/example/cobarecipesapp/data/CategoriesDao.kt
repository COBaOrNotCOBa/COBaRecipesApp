package com.example.cobarecipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cobarecipesapp.model.Category


@Dao
interface CategoriesDao {
    @Query("SELECT * FROM category")
    suspend fun getCategories(): List<Category>

    @Query("SELECT * FROM category WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(vararg category: Category)

}