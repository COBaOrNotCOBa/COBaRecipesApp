package com.example.cobarecipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.databinding.FragmentListCategoriesBinding
import com.example.cobarecipesapp.ui.common.navigateWithAnimation


class CategoriesListFragment : Fragment(R.layout.fragment_list_categories) {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    private val categoriesViewModel: CategoryListViewModel by viewModels()
    private lateinit var categoriesAdapter: CategoriesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesViewModel.loadCategories()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        initAdapter()
        initObserve()
    }

    private fun initAdapter() {
        categoriesAdapter = CategoriesListAdapter()
        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun initObserve() {
        categoriesViewModel.categoriesState.observe(viewLifecycleOwner) { state ->
            categoriesAdapter.updateData(state.categories)
        }
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = categoriesViewModel.loadCategoryById(categoryId)
            ?: throw IllegalStateException("Category with id $categoryId not found")
        val action =
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                category
            )
        findNavController().navigateWithAnimation(action)
    }

}