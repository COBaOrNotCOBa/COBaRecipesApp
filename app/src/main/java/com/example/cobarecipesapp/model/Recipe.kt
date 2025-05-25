package com.example.cobarecipesapp.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import androidx.room.PrimaryKey


@Entity(tableName = "recipes")
@Serializable
@Parcelize
data class Recipe(
    @PrimaryKey val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String,
//    val categoryId: Int,
) : Parcelable