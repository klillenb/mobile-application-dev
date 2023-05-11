package com.example.mobileapp.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.databinding.FragmentRecipesBinding
import com.example.mobileapp.dto.RecipeDto
import com.example.mobileapp.model.SharedViewModel

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    //ühised andmed
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val homeViewModel =
            ViewModelProvider(this)[sharedViewModel::class.java]

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.recyclerViewRecipes
        recyclerView.layoutManager = LinearLayoutManager(activity)


        val recipeAdapter = RecipeAdapter(){recipe ->
            Toast.makeText(activity, recipe._id, Toast.LENGTH_SHORT).show()
            homeViewModel.toggleFave(recipe)
        }

        recyclerView.adapter = recipeAdapter


        val recipeDtoObserver = Observer<List<RecipeDto>>{ recipes ->
            recipeAdapter.submitList(recipes)
        }
        homeViewModel.recipeList.observe(viewLifecycleOwner, recipeDtoObserver )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}