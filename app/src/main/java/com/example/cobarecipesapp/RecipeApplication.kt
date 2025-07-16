package com.example.cobarecipesapp

import android.app.Application
import com.example.cobarecipesapp.di.AppContainer

class RecipesApplication: Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }

}