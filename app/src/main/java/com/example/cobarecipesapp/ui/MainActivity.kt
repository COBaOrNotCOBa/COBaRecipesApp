package com.example.cobarecipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.databinding.ActivityMainBinding
import com.example.cobarecipesapp.model.Category
import com.example.cobarecipesapp.ui.common.navigateWithAnimation
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val threadNameMainActivity = Thread.currentThread().name
        Log.i("!!!", "Метод onCreate() выполняется на потоке: $threadNameMainActivity")

        val thread = Thread {
            val threadName = Thread.currentThread().name
            Log.i("!!!", "Выполняю запрос на потоке: $threadName")

            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.connect()

            val responseString = connection.inputStream.bufferedReader().readText()
            Log.i("!!!", "responseCode: ${connection.responseCode}")
            Log.i("!!!", "responseMessage: ${connection.responseMessage}")
            Log.i("!!!", "Body: $responseString")

            val json = Json { ignoreUnknownKeys = true }

            val categories = json.decodeFromString<List<Category>>(responseString)
            Log.i("!!!", "Categories: $categories")

        }

        thread.start()


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
}