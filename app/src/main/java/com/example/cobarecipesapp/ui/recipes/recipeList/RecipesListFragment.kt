package com.example.cobarecipesapp.ui.recipes.recipeList

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.data.STUB
import com.example.cobarecipesapp.databinding.FragmentListRecipesBinding
import com.example.cobarecipesapp.ui.categories.CategoriesListFragment
import com.example.cobarecipesapp.ui.recipes.recipe.RecipeFragment
import com.example.cobarecipesapp.ui.common.setOnItemClick

class RecipesListFragment : Fragment(R.layout.fragment_list_recipes) {

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

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

        initUI(view)

        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBundleData() {
        arguments?.let { argument ->
            categoryId = argument.getInt(CategoriesListFragment.ARG_CATEGORY_ID)
            categoryName = argument.getString(CategoriesListFragment.ARG_CATEGORY_NAME)
            categoryImageUrl = argument.getString(CategoriesListFragment.ARG_CATEGORY_IMAGE_URL)
        }
    }

    private fun initUI(view: View) {

        binding.tvRecipesCategory.text = categoryName

        val drawable = try {
            Drawable.createFromStream(
                categoryImageUrl?.let { view.context.assets.open(it) },
                null
            )
        } catch (e: Exception) {
            Log.e("ImageLoadError", "Image not found: $categoryName", e)
            null
        }
        binding.ivRecipesHeader.setImageDrawable(drawable)
    }

    private fun initRecycler() {
        val recipes = categoryId?.let { STUB.getRecipesByCategoryId(it) } ?: emptyList()
        val recipesAdapter = RecipesListAdapter(recipes)
        binding.rvRecipes.adapter = recipesAdapter

        recipesAdapter.setOnItemClick { recipeId ->
            openRecipeByRecipeId(recipeId)
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