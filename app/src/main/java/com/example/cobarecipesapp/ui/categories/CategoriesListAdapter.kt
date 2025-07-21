package com.example.cobarecipesapp.ui.categories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.databinding.ItemCategoryBinding
import com.example.cobarecipesapp.model.Category
import com.example.cobarecipesapp.utils.UrlHelper


class CategoriesListAdapter(
    private val urlHelper: UrlHelper,
    private var dataSet: List<Category> = emptyList()
) : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

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

                val categoryUrl = urlHelper.getFullImageUrl(category.imageUrl)
                loadCategoryImage(categoryUrl)

                val description = itemView.context.getString(
                    R.string.image_category_description,
                    category.title
                )
                ivCategoryImage.contentDescription = description

                root.setOnClickListener { itemClickListener?.onItemClick(category.id) }
            }
        }

        private fun loadCategoryImage(categoryUrl: String) {
            Glide.with(itemView.context)
                .load(categoryUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .centerCrop()
                .into(binding.ivCategoryImage)
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