package com.example.cobarecipesapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.databinding.ActivityMainBinding
import com.example.cobarecipesapp.ui.categories.CategoriesListFragment
import com.example.cobarecipesapp.ui.recipes.favorites.FavoritesFragment

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.mainContainer, CategoriesListFragment())

            }
        }

        binding.btnCategories.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack(null)
                replace<CategoriesListFragment>(R.id.mainContainer)
            }
        }

        binding.btnFavourites.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack(null)
                replace<FavoritesFragment>(R.id.mainContainer)
            }
        }
    }
}