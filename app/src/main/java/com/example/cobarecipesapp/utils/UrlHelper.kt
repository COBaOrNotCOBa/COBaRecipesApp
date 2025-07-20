package com.example.cobarecipesapp.utils

class UrlHelper(private val baseUrl: String) {
    fun getFullImageUrl(imageName: String) = "$baseUrl/images/$imageName"
}