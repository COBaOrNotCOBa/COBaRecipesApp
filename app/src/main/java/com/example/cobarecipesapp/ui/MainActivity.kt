package com.example.cobarecipesapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.databinding.ActivityMainBinding
import com.example.cobarecipesapp.ui.common.navigateWithAnimation
import com.example.cobarecipesapp.utils.ToastHelper
import dagger.hilt.android.AndroidEntryPoint
import android.content.pm.ActivityInfo


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initEdgeToEdge()
        ToastHelper.init(application)
        initNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initEdgeToEdge() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initNavigation() {
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