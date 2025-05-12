package com.example.cobarecipesapp.ui.recipes.recipeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.databinding.FragmentListRecipesBinding


class RecipesListFragment : Fragment(R.layout.fragment_list_recipes) {

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    private val recipesListViewModel: RecipesListViewModel by viewModels()
    private val argsRecipeListFragment: RecipesListFragmentArgs by navArgs()
    private lateinit var recipesAdapter: RecipesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBundleData()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBundleData() {
        val category = argsRecipeListFragment.category
        recipesListViewModel.loadRecipeList(category.id, category.title, category.imageUrl)
    }

    private fun initUI() {
        recipesAdapter = RecipesListAdapter()
        initRecycler()
        initObserve()
    }

    private fun initRecycler() {
        binding.rvRecipes.adapter = recipesAdapter
        recipesAdapter.setOnItemClick { recipeId ->
            openRecipeByRecipeId(recipeId)
        }
    }

    private fun initObserve() {
        recipesListViewModel.recipesListState.observe(viewLifecycleOwner) { state ->
            binding.tvRecipesCategory.text = state.categoryName
            binding.ivRecipesHeader.setImageDrawable(state.categoryImage)
            recipesAdapter.updateRecipes(state.recipes)
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action =
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }
}