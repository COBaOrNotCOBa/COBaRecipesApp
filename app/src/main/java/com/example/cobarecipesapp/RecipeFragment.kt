package com.example.cobarecipesapp

import android.annotation.SuppressLint
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
import com.example.cobarecipesapp.domain.Recipe
import com.google.android.material.divider.MaterialDividerItemDecoration

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
        binding.tvRecipeNameHeader.text = recipe.title

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

    private fun initRecycler() {
        with(binding) {
            val recipe = STUB.getRecipeById(recipe.id)

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

    private fun createDividerDecoration(): MaterialDividerItemDecoration {
        return MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
            dividerColor =
                ContextCompat.getColor(requireContext(), R.color.line_ingredient_color)
            dividerThickness =
                resources.getDimensionPixelSize(R.dimen.divider_height)
            isLastItemDecorated = false
        }
    }

}