package com.example.cobarecipesapp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cobarecipesapp.RecipesListFragment.Companion.ARG_RECIPE
import com.example.cobarecipesapp.databinding.FragmentRecipeBinding
import com.example.cobarecipesapp.domain.Recipe

class RecipeFragment : Fragment(R.layout.fragment_recipe) {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    private lateinit var recipe: Recipe

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipe = initBundleData()

        binding.tvRecipeNameHeader.text = recipe.title

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBundleData(): Recipe {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_RECIPE)
        } ?: throw IllegalStateException("Recipe not found in arguments")
    }
}