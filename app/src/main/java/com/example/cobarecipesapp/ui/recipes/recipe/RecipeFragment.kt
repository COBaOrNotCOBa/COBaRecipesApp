package com.example.cobarecipesapp.ui.recipes.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cobarecipesapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.cobarecipesapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class RecipeFragment : Fragment(R.layout.fragment_recipe) {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    private val recipeViewModel: RecipeViewModel by viewModels()
    private val argsRecipeFragment: RecipeFragmentArgs by navArgs()
    private lateinit var ingredientAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBundleData() {
        val recipeId = argsRecipeFragment.recipeId
        recipeViewModel.loadRecipe(recipeId)
    }

    private fun initUI() {
        initAdapters()
        initRecycler()
        initObserve()
        initSeekBar()
    }

    private fun initAdapters() {
        ingredientAdapter = IngredientsAdapter()
        methodAdapter = MethodAdapter()
    }

    private fun initRecycler() {
        with(binding) {
            val ingredientsDecoration = createDividerDecoration()
            val methodDecoration = createDividerDecoration()

            rvIngredients.addItemDecoration(ingredientsDecoration)
            rvMethod.addItemDecoration(methodDecoration)

            rvIngredients.adapter = ingredientAdapter
            rvMethod.adapter = methodAdapter
        }
    }

    private fun initObserve() {
        recipeViewModel.recipeState.observe(viewLifecycleOwner) { state ->
            with(binding) {
                state.recipe?.let { recipe ->
                    if (binding.tvRecipeNameHeader.text.isEmpty()) {
                        tvRecipeNameHeader.text = recipe.title
                        loadRecipeImage(state.recipeImageUrl)
                    }
                    updateHeartIconState(recipe.isFavorite)
                    sbPortionsCount.progress = state.portionsCount
                    tvPortionsCount.text = state.portionsCount.toString()

                    ingredientAdapter.apply {
                        updateData(recipe.ingredients)
                        updateIngredients(state.portionsCount)
                    }
                    methodAdapter.updateData(recipe.method)

                    ibHeartIcon.setOnClickListener { recipeViewModel.onFavoritesClicked() }
                }
            }
        }

        recipeViewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                recipeViewModel.clearToastMessage()
            }
        }
    }

    private fun initSeekBar() {
        binding.sbPortionsCount.setOnSeekBarChangeListener(PortionSeekBarListener { progress ->
            recipeViewModel.updatePortionsCount(progress)
        })
    }

    private fun loadRecipeImage(recipeImageUrl: String?) {
        Glide.with(requireContext())
            .load(recipeImageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .centerCrop()
            .into(binding.ivRecipeImageHeader)
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

    inner class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) :
        OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            Log.d("SeekBar", "Начало перемещения ползунка")
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            Log.d("SeekBar", "Конец перемещения ползунка")
        }
    }

}