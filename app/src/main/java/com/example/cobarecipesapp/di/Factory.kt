package com.example.cobarecipesapp.di

interface Factory<T> {
    fun create(): T
}