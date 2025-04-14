package com.example.cobarecipesapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cobarecipesapp.databinding.ItemIngredientBinding
import com.example.cobarecipesapp.domain.Ingredient

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity = 1

    inner class ViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(ingredient: Ingredient) {
            with(binding) {

                tvIngredientDescription.text = ingredient.description
                tvIngredientQuantity.text = checkQuantityType(ingredient)
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
    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    @SuppressLint("DefaultLocale")
    private fun checkQuantityType(ingredient: Ingredient): String {
        val quantityValue = ingredient.quantity.toDouble() * quantity
        return if (quantityValue % 1 == 0.0) {
            quantityValue.toInt().toString()
        } else {
            "%.1f".format(quantityValue)
        }
    }
}