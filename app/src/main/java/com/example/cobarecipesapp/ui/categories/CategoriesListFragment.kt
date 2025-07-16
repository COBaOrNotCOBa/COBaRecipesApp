package com.example.cobarecipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cobarecipesapp.R
import com.example.cobarecipesapp.RecipesApplication
import com.example.cobarecipesapp.databinding.FragmentListCategoriesBinding
import com.example.cobarecipesapp.di.AppContainer
import com.example.cobarecipesapp.ui.common.navigateWithAnimation


class CategoriesListFragment : Fragment(R.layout.fragment_list_categories) {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding is null")

    private lateinit var categoriesListViewModel: CategoriesListViewModel
    private lateinit var categoriesAdapter: CategoriesListAdapter
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContainer = (requireActivity().application as RecipesApplication).appContainer
        categoriesListViewModel = appContainer.categoriesListViewModelFactory.create()
    }

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

        categoriesListViewModel.clearNavigation()
        categoriesListViewModel.loadCategories()
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
//        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        categoriesAdapter = CategoriesListAdapter(appContainer.repository)
        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun initObserve() {
        categoriesListViewModel.categoriesState.observe(viewLifecycleOwner) { state ->
            state.categories.let {
                categoriesAdapter.updateData(state.categories)
            }
        }

        categoriesListViewModel.selectedCategory.observe(viewLifecycleOwner) { category ->
            category?.let {
                val action = CategoriesListFragmentDirections
                    .actionCategoriesListFragmentToRecipesListFragment(it)
                findNavController().navigateWithAnimation(action)
                categoriesListViewModel.clearNavigation()
            }
        }

        categoriesListViewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                categoriesListViewModel.clearToastMessage()
            }
        }
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        categoriesListViewModel.loadCategoryById(categoryId)
    }
}