package com.example.cobarecipesapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String,
) : Parcelable {

//    constructor(parcel: Parcel): this(
//        id = parcel.readInt(),
//        title = parcel.readString() ?: "",
//        ingredients = mutableListOf<Ingredient>().apply {
//            parcel.readTypedList(this, Ingredient.CREATOR)
//        },
//        method = parcel.createStringArrayList() ?: emptyList(),
//        imageUrl = parcel.readString() ?: ""
//    )
//
//    companion object : Parceler<Recipe> {
//
//        override fun Recipe.write(parcel: Parcel, flags: Int) {
//            parcel.writeInt(id)
//            parcel.writeString(title)
//            parcel.writeList(ingredients)
//            parcel.writeList(method)
//            parcel.writeString(imageUrl)
//        }
//
//        override fun create(parcel: Parcel): Recipe {
//            return Recipe(parcel)
//        }
//
//    }
}