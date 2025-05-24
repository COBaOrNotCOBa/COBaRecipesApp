package com.example.cobarecipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cobarecipesapp.model.Category

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM category")
    fun getCategories(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategories(vararg category: Category)
}