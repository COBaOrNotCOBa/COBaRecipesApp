package com.example.cobarecipesapp.ui.recipes.recipeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.databinding.FragmentListRecipesBinding
import com.example.cobarecipesapp.ui.common.navigateWithAnimation


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
        val categoryId = argsRecipeListFragment.category.id
        recipesListViewModel.loadRecipeList(categoryId)
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
            loadRecipeImage(state.categoryImageUrl)
            recipesAdapter.updateRecipes(state.recipes)
        }
    }

    private fun loadRecipeImage(categoryImageUrl: String?) {
        categoryImageUrl?.let { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .centerCrop()
                .into(binding.ivRecipesHeader)
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action =
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)
        findNavController().navigateWithAnimation(action)
    }
}