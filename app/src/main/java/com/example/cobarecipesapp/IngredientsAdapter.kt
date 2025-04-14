package com.example.cobarecipesapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cobarecipesapp.databinding.ItemIngredientBinding
import com.example.cobarecipesapp.domain.Ingredient

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(ingredient: Ingredient) {
            with(binding) {

                tvIngredientDescription.text = ingredient.description
                tvIngredientQuantity.text = ingredient.quantity
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

}