package com.example.cobarecipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cobarecipesapp.databinding.ItemCategoryBinding
import com.example.cobarecipesapp.domain.Category

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            with(binding) {
                tvTitle.text = category.title
                tvDescription.text = category.description

                val drawable = try {
                    Drawable.createFromStream(
                        itemView.context.assets.open(category.imageUrl),
                        null
                    )
                } catch (e: Exception) {
                    Log.e(
                        "ImageLoadError",
                        "Image not found: ${category.imageUrl}\n${Log.getStackTraceString(e)}"
                    )
                    null
                }
                imageCategory.setImageDrawable(drawable)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}