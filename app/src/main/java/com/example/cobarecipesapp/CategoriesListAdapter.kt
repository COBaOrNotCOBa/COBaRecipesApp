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
                    R.string.category_image_description,
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

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
//        viewHolder.itemView.setOnClickListener {
//            itemClickListener?.onItemClick(dataSet[position].id)
//        }
    }

    override fun getItemCount() = dataSet.size

}