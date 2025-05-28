package com.example.cobarecipesapp.data

import androidx.room.TypeConverter
import com.example.cobarecipesapp.model.Ingredient
import kotlinx.serialization.json.Json

class TypeConverter {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @TypeConverter
    fun fromIngredientsList(ingredients: List<Ingredient>): String {
        return json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredientsList(jsonString: String): List<Ingredient> {
        return try {
            json.decodeFromString(jsonString)
        } catch (_: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromMethodList(method: List<String>): String {
        return json.encodeToString(method)
    }

    @TypeConverter
    fun toMethodList(jsonString: String): List<String> {
        return try {
            json.decodeFromString(jsonString)
        } catch (_: Exception) {
            emptyList()
        }
    }
}