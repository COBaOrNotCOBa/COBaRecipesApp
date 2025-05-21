package com.example.cobarecipesapp.data

import android.app.Application
import com.example.cobarecipesapp.utils.ToastHelper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ThreadPoolApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ToastHelper.init(this)
    }

    companion object {
        private const val SIZE_THREAD_POOL = 10
        val threadPool: ExecutorService = Executors.newFixedThreadPool(SIZE_THREAD_POOL)
    }
}