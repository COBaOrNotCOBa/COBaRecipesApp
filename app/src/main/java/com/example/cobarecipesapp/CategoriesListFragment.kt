package com.example.cobarecipesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.cobarecipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment(R.layout.fragment_list_categories) {

    private lateinit var binding: FragmentListCategoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentListCategoriesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}