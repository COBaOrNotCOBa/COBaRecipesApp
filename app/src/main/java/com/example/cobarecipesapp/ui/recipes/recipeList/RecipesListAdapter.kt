package com.example.cobarecipesapp.ui.recipes.recipeList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.data.RecipesRepository
import com.example.cobarecipesapp.databinding.ItemRecipeBinding
import com.example.cobarecipesapp.model.Recipe


class RecipesListAdapter(
//    private val urlHelper: UrlHelper,
//    private val recipesRepository: RecipesRepository,
    private var dataSet: List<Recipe> = emptyList()
) : RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

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
            binding.tvRecipeTitle.text = recipe.title

//            val imageUrl2 = recipesRepository.getFullImageUrl(recipe.imageUrl)
            val imageUrl = RecipesRepository.BASE_IMAGES_URL + recipe.imageUrl
            loadRecipeImage(imageUrl)

            val description = itemView.context.getString(
                R.string.image_recipe_description,
                recipe.title
            )
            binding.ivRecipeImage.contentDescription = description

            binding.root.setOnClickListener { itemClickListener?.onItemClick(recipe.id) }
        }

        private fun loadRecipeImage(imageUrl: String) {
            Glide.with(itemView.context)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .centerCrop()
                .into(binding.ivRecipeImage)
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

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecipes(newDataSet: List<Recipe>) {
        dataSet = newDataSet
        notifyDataSetChanged()
    }

}