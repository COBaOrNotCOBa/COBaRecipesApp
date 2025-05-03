package com.example.cobarecipesapp.ui.recipes.recipe

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cobarecipesapp.databinding.ItemIngredientBinding
import com.example.cobarecipesapp.model.Ingredient
import com.example.cobarecipesapp.utils.multiply
import com.example.cobarecipesapp.utils.toRoundedString


class IngredientsAdapter(var dataSet: List<Ingredient> = emptyList()) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    companion object {
        const val MIN_AMOUNT_OF_PORTIONS = 1
    }

    private var quantity = MIN_AMOUNT_OF_PORTIONS

    inner class ViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(ingredient: Ingredient) {
            with(binding) {
                tvIngredientDescription.text = ingredient.description
                tvIngredientQuantity.text = calculateQuantity(ingredient.quantity)
                tvIngredientUnitOfMeasure.text = ingredient.unitOfMeasure
            }
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemIngredientBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(dataSet[position])

    override fun getItemCount(): Int = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newDataSet: List<Ingredient>) {
        dataSet = newDataSet
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateIngredients(progress: Int) {
        if (quantity != progress) {
            quantity = progress
            notifyDataSetChanged()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun calculateQuantity(ingredientQuantity: String): String =
        ingredientQuantity.multiply(quantity).toRoundedString()

}