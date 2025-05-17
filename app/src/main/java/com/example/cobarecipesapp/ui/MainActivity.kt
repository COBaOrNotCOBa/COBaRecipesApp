package com.example.cobarecipesapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.databinding.ActivityMainBinding
import com.example.cobarecipesapp.model.Category
import com.example.cobarecipesapp.ui.common.navigateWithAnimation
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    private val sizeThreadPool = 10
    private val threadPool = Executors.newFixedThreadPool(sizeThreadPool)

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    private val json = Json { ignoreUnknownKeys = true }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadCategories()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCategories.setOnClickListener {
            findNavController(R.id.navHostFragment).navigateWithAnimation(
                R.id.categoriesListFragment
            )
        }

        binding.btnFavourites.setOnClickListener {
            findNavController(R.id.navHostFragment).navigateWithAnimation(
                R.id.favoritesFragment,
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
        _binding = null
    }

    private fun loadCategories() {
        threadPool.execute {
            val request: Request = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string() ?: return@execute
                val categories = json.decodeFromString<List<Category>>(responseBody)
                val categoryListId = categories.map { it.id }
                loadRecipeList(categoryListId)
            }
        }
    }

    private fun loadRecipeList(categoryListId: List<Int>) {
        categoryListId.forEach { id ->
            threadPool.execute {
                val request: Request = Request.Builder()
                    .url("https://recipes.androidsprint.ru/api/recipes?ids=$id")
                    .build()
                client.newCall(request).execute()
            }
        }
    }

}