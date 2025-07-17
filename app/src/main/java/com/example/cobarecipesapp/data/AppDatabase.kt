package com.example.cobarecipesapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cobarecipesapp.model.Category
import com.example.cobarecipesapp.model.Recipe
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(entities = [Category::class, Recipe::class], version = 3, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao

    companion object {
        private const val DATABASE_NAME = "recipes-database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        @Provides
        fun getDatabase(@ApplicationContext context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}