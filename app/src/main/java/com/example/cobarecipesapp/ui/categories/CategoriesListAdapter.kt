package com.example.cobarecipesapp.ui.categories

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.databinding.ItemCategoryBinding
import com.example.cobarecipesapp.model.Category

class CategoriesListAdapter(private var dataSet: List<Category> = emptyList()) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            with(binding) {
                tvCategoryTitle.text = category.title
                tvCategoryDescription.text = category.description

                val drawable = try {
                    Drawable.createFromStream(
                        itemView.context.assets.open(category.imageUrl),
                        null
                    )
                } catch (e: Exception) {
                    Log.e("ImageLoadError", "Image not found: ${category.title}", e)
                    null
                }
                ivCategoryImage.setImageDrawable(drawable)

                val description = itemView.context.getString(
                    R.string.image_category_description,
                    category.title
                )
                ivCategoryImage.contentDescription = description

                root.setOnClickListener { itemClickListener?.onItemClick(category.id) }

            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(dataSet[position])

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newDataSet: List<Category>) {
        dataSet = newDataSet
        notifyDataSetChanged()
    }

}