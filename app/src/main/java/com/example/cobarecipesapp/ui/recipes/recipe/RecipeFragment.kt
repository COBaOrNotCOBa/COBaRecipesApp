package com.example.cobarecipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cobarecipesapp.databinding.FragmentRecipeBinding
import com.example.cobarecipesapp.model.Recipe
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.core.content.edit
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.ui.recipes.recipeList.RecipesListFragment

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

        initBundleData()

        initUI(view)

        initRecycler()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBundleData() {
        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(RecipesListFragment.ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(RecipesListFragment.ARG_RECIPE)
        } ?: throw IllegalStateException("Recipe not found in arguments")
    }

    private fun initUI(view: View) {
        with(binding) {
            tvRecipeNameHeader.text = recipe.title
            loadRecipeImage(view)
            setupFavoriteToggle()
        }
    }

    private fun initRecycler() {
        with(binding) {

            val ingredientAdapter = IngredientsAdapter(recipe.ingredients)
            rvIngredients.adapter = ingredientAdapter
            rvIngredients.addItemDecoration(createDividerDecoration())

            val methodAdapter = MethodAdapter(recipe.method)
            rvMethod.adapter = methodAdapter
            rvMethod.addItemDecoration(createDividerDecoration())

            sbPortionsCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    tvPortionsCount.text = progress.toString()
                    ingredientAdapter.updateIngredients(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Log.d("SeekBar", "Начало перемещения ползунка")
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    Log.d("SeekBar", "Конец перемещения ползунка")
                }
            })
        }
    }

    private fun loadRecipeImage(view: View) {
        val drawable = try {
            Drawable.createFromStream(
                recipe.imageUrl.let { view.context.assets.open(it) },
                null
            )
        } catch (e: Exception) {
            Log.e("ImageLoadError", "Image not found: ${recipe.title}", e)
            null
        }
        binding.ivRecipeImageHeader.setImageDrawable(drawable)
    }

    private fun setupFavoriteToggle() {

        updateHeartIconState()

        binding.ibHeartIcon.setOnClickListener {
            val currentFavoriteRecipes = getFavorites()
            val recipeId = recipe.id.toString()
            val isFavorite = currentFavoriteRecipes.contains(recipeId)

            if (isFavorite) {
                currentFavoriteRecipes.remove(recipeId)
            } else {
                currentFavoriteRecipes.add(recipeId)
            }

            saveFavorites(currentFavoriteRecipes)
            updateHeartIconState()
        }
    }

    private fun updateHeartIconState() {
        val isFavorite = getFavorites().contains(recipe.id.toString())
        binding.ibHeartIcon.setImageResource(
            if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
        )
    }

    private fun saveFavorites(favoritesId: Set<String>) {
        val sharedPrefs = requireContext().getSharedPreferences(
            favorite_prefs_key, Context.MODE_PRIVATE
        ) ?: return

        sharedPrefs.edit(commit = true) {
            putStringSet(FAVORITE_RECIPES_KEY, favoritesId)
        }

    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = requireContext().getSharedPreferences(
            favorite_prefs_key, Context.MODE_PRIVATE
        )
        return HashSet(sharedPrefs?.getStringSet(FAVORITE_RECIPES_KEY, HashSet()) ?: mutableSetOf())

    }

    private fun createDividerDecoration(): MaterialDividerItemDecoration {
        return MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
            dividerColor =
                ContextCompat.getColor(requireContext(), R.color.line_ingredient_color)
            dividerThickness =
                resources.getDimensionPixelSize(R.dimen.divider_height)
            isLastItemDecorated = false
        }
    }

    companion object {
        const val FAVORITE_RECIPES_KEY = "favorite_recipes_key"
        const val favorite_prefs_key = "favorite prefs key"
    }

}