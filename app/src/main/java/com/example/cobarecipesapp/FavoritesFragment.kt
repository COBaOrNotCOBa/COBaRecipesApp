package com.example.cobarecipesapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.cobarecipesapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")
    private lateinit var favoritesAdapter: RecipesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        loadFavorites()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        favoritesAdapter = RecipesListAdapter(emptyList())

        binding.rvFavorites.adapter = favoritesAdapter

        favoritesAdapter.setOnItemClick { recipeId ->
            openRecipeByRecipeId(recipeId)
        }

    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val recipe = STUB.getRecipeById(recipeId)
        val bundle = bundleOf(RecipesListFragment.ARG_RECIPE to recipe)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
        }
    }

    private fun loadFavorites() {
        val favoritesId = getFavorites().map { it.toInt() }.toSet()

        with(binding) {
            if (favoritesId.isEmpty()) {
                tvEmptyState.visibility = View.VISIBLE
                rvFavorites.visibility = View.GONE
            } else {
                val favoriteRecipes = STUB.getRecipesByIds(favoritesId)
                favoritesAdapter = RecipesListAdapter(favoriteRecipes)
                favoritesAdapter.setOnItemClick { recipeId ->
                    openRecipeByRecipeId(recipeId)
                }
                rvFavorites.adapter = favoritesAdapter
                tvEmptyState.visibility = View.GONE
                rvFavorites.visibility = View.VISIBLE
            }
        }
    }



    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = requireContext().getSharedPreferences(
            RecipeFragment.FAVORITE_PREFS_KEY, Context.MODE_PRIVATE
        )
        return HashSet(
            sharedPrefs?.getStringSet(RecipeFragment.FAVORITE_RECIPES_KEY, HashSet())
                ?: mutableSetOf()
        )

    }

}