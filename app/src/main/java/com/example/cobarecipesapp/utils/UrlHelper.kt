package com.example.cobarecipesapp.utils

import javax.inject.Inject

class UrlHelper @Inject constructor(private val baseImageUrl : String) {
    fun getFullImageUrl(imageName: String): String {
        return "$baseImageUrl$imageName"
    }
}