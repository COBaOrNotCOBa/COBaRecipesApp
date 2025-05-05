package com.example.cobarecipesapp.ui.recipes.recipeList

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.databinding.ItemRecipeBinding
import com.example.cobarecipesapp.model.Recipe

class RecipesListAdapter(private var dataSet: List<Recipe> = emptyList()) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    private fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            with(binding) {
                tvRecipeTitle.text = recipe.title

                val drawable = try {
                    Drawable.createFromStream(
                        itemView.context.assets.open(recipe.imageUrl),
                        null
                    )
                } catch (e: Exception) {
                    Log.e("ImageLoadError", "Image not found: ${recipe.title}", e)
                    null
                }
                ivRecipeImage.setImageDrawable(drawable)

                val description = itemView.context.getString(
                    R.string.image_recipe_description,
                    recipe.title
                )
                ivRecipeImage.contentDescription = description

                root.setOnClickListener { itemClickListener?.onItemClick(recipe.id) }
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecipeBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(dataSet[position])


    override fun getItemCount() = dataSet.size

    fun setOnItemClick(listener: (Int) -> Unit) {
        setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(recipeId: Int) = listener(recipeId)
        })
    }

    private fun updateRecipes(){

    }

}