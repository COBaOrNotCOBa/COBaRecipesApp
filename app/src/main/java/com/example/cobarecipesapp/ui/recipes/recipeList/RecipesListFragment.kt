package com.example.cobarecipesapp.ui.recipes.recipeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.databinding.FragmentListRecipesBinding
import com.example.cobarecipesapp.ui.categories.CategoriesListFragment
import com.example.cobarecipesapp.ui.recipes.recipe.RecipeFragment


class RecipesListFragment : Fragment(R.layout.fragment_list_recipes) {

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    private val recipesListViewModel: RecipesListViewModel by viewModels()
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
        val args = arguments
        val categoryId =
            args?.getInt(CategoriesListFragment.ARG_CATEGORY_ID) ?: throw IllegalStateException(
                "Category ID not found in arguments"
            )
        val categoryName = args.getString(CategoriesListFragment.ARG_CATEGORY_NAME)
            ?: throw IllegalStateException("Category name not found in arguments")
        val categoryImageUrl = args.getString(CategoriesListFragment.ARG_CATEGORY_IMAGE_URL)
            ?: throw IllegalStateException("Category image URL not found in arguments")

        recipesListViewModel.loadRecipeList(categoryId, categoryName, categoryImageUrl)
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
        val bundle = bundleOf(RecipeFragment.ARG_RECIPE_ID to recipeId)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
        }
    }
}