package com.example.cobarecipesapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
@Parcelize
data class Category(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
) : Parcelable