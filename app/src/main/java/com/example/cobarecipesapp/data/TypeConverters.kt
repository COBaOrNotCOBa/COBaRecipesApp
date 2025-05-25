package com.example.cobarecipesapp.data

import androidx.room.TypeConverter
import com.example.cobarecipesapp.model.Ingredient
import kotlinx.serialization.json.Json

class TypeConverter {
    @TypeConverter
    fun fromIngredientsList(ingredients: List<Ingredient>): String {
        return Json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredientsList(json: String): List<Ingredient> {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String = list.joinToString("|")

    @TypeConverter
    fun toStringList(data: String): List<String> = data.split("|")
}