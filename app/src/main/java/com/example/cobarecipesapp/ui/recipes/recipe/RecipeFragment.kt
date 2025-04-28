package com.example.cobarecipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
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
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.fragment.app.viewModels
import com.example.cobarecipesapp.R


class RecipeFragment : Fragment(R.layout.fragment_recipe) {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    private val recipeViewModel: RecipeViewModel by viewModels()
    private lateinit var ingredientAdapter: IngredientsAdapter

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
        initUI()
        initRecycler()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBundleData() {
        val recipeId = arguments?.getInt(ARG_RECIPE_ID)
            ?: throw IllegalStateException("Recipe ID not found in arguments");
        recipeViewModel.loadRecipe(recipeId)
    }

    private fun initUI() {
        recipeViewModel.recipeState.observe(viewLifecycleOwner) { state ->
            state.recipe?.let { recipe ->

                with(binding) {
                    tvRecipeNameHeader.text = recipe.title
                    loadRecipeImage(recipe.imageUrl)
                    updateHeartIconState(state.isFavorite)

                    if (sbPortionsCount.progress != state.portionsCount) {
                        sbPortionsCount.progress = state.portionsCount
                        tvPortionsCount.text = state.portionsCount.toString()
                    }

                    ibHeartIcon.setOnClickListener { recipeViewModel.onFavoritesClicked() }

                    Log.i(
                        "!!!",
                        "isFavorite = ${state.isFavorite}"
                    )
                }
            }
        }
    }

    private fun initRecycler() {
        with(binding) {
            val ingredientsDecoration = createDividerDecoration()
            val methodDecoration = createDividerDecoration()

            rvIngredients.addItemDecoration(ingredientsDecoration)
            rvMethod.addItemDecoration(methodDecoration)

            val recipe = recipeViewModel.recipeState.value?.recipe
            val ingredients = recipe?.ingredients ?: emptyList()
            val method = recipe?.method ?: emptyList()

            ingredientAdapter = IngredientsAdapter(ingredients)
            rvIngredients.adapter = ingredientAdapter
            rvMethod.adapter = MethodAdapter(method)

            sbPortionsCount.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    recipeViewModel.updatePortionsCount(progress)
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

    private fun loadRecipeImage(imageUrl: String) {
        val drawable = try {
            Drawable.createFromStream(view?.context?.assets?.open(imageUrl), null)
        } catch (e: Exception) {
            Log.e(
                "ImageLoadError",
                "Image not found: ${recipeViewModel.recipeState.value?.recipe?.title}",
                e
            )
            null
        }
        binding.ivRecipeImageHeader.setImageDrawable(drawable)
    }

    private fun updateHeartIconState(isFavorite: Boolean) {
        binding.ibHeartIcon.setImageResource(
            if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
        )
    }

    private fun createDividerDecoration(): MaterialDividerItemDecoration {
        return MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        ).apply {
            dividerColor =
                ContextCompat.getColor(requireContext(), R.color.line_ingredient_color)
            dividerThickness =
                resources.getDimensionPixelSize(R.dimen.divider_height)
            isLastItemDecorated = false
        }
    }

    companion object {
        const val ARG_RECIPE_ID = "arg_recipe_id"
    }

}