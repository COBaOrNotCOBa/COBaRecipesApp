package com.example.cobarecipesapp.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo


@Entity(tableName = "recipes")
@Serializable
@Parcelize
data class Recipe(
    @PrimaryKey val id: Int,
    val title: String,
    @ColumnInfo("ingredients_json")
    val ingredients: List<Ingredient>,
    @ColumnInfo("method_json")
    val method: List<String>,
    val imageUrl: String,
    val categoryId: Int = -1,
    val isFavorite: Boolean = false,
) : Parcelable