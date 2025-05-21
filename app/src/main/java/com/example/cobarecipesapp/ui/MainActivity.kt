package com.example.cobarecipesapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.databinding.ActivityMainBinding
import com.example.cobarecipesapp.ui.common.navigateWithAnimation


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        _binding = null
    }

}