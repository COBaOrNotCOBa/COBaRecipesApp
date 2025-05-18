package com.example.cobarecipesapp.utils

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast

object ToastHelper {
    private lateinit var appContext: Application

    fun init(application: Application) {
        appContext = application
    }

    fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}