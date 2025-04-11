package com.example.cobarecipesapp

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
import com.example.cobarecipesapp.databinding.FragmentListRecipesBinding
import java.lang.IllegalStateException

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

        recipesAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                categoryId?.let { openRecipeByRecipeId(recipeId) }
            }
        })

    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            replace<RecipeFragment>(R.id.mainContainer)
        }
    }

}