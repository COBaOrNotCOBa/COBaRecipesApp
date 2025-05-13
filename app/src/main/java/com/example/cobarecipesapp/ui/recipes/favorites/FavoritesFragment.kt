package com.example.cobarecipesapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.databinding.FragmentFavoritesBinding
import com.example.cobarecipesapp.ui.recipes.recipeList.RecipesListAdapter


class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    private val favoritesViewModel: FavoritesViewModel by viewModels()
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

        favoritesViewModel.loadFavorites()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        favoritesAdapter = RecipesListAdapter()
        initRecycler()
        initObserve()
    }

    private fun initRecycler() {
        binding.rvFavorites.adapter = favoritesAdapter
        favoritesAdapter.setOnItemClick { recipeId ->
            openRecipeByRecipeId(recipeId)
        }
    }

    private fun initObserve() {
        favoritesViewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            with(binding) {
                if (state.recipes.isEmpty()) {
                    tvEmptyState.visibility = View.VISIBLE
                    rvFavorites.visibility = View.GONE
                } else {
                    favoritesAdapter.updateRecipes(state.recipes)
                    favoritesAdapter.setOnItemClick { recipeId ->
                        openRecipeByRecipeId(recipeId)
                    }
                    rvFavorites.adapter = favoritesAdapter
                    tvEmptyState.visibility = View.GONE
                    rvFavorites.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action =
            FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }

    companion object {
        const val FAVORITE_RECIPES_KEY = "favorite_recipes_key"
        const val FAVORITE_PREFS_KEY = "favorite_prefs_key"
    }
}