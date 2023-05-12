package com.example.mobileapp.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentRecipesBinding
import com.example.mobileapp.dto.RecipeDto

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //private val recipeRepository = RecipeRepository()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val homeViewModel =
            ViewModelProvider(this)[RecipesViewModel::class.java]

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.recyclerViewRecipes
        recyclerView.layoutManager = LinearLayoutManager(activity)

        homeViewModel.getData()
        // homeViewModel.recipeList.observe(viewLifecycleOwner) {
            // textView.text = it
        // }

        val recipeAdapter = RecipeAdapter(){recipe ->
            //val msg = getString(R.string.forecast_clicked_format, forecastItem.temp, forecastItem.description)
            showRecipeDetail(recipe)
            //showForecastDetails(forecast)
            //TODO sisesta retseptivaade (intentiga?)
        }

        recyclerView.adapter = recipeAdapter

        val recipeDtoObserver = Observer<List<RecipeDto>>{ recipes ->
            //forecastItem update our list adapter
            recipeAdapter.submitList(recipes)
        }
        homeViewModel.recipeList.observe(this, recipeDtoObserver)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showRecipeDetail(recipe: RecipeDto) {
        val recipeDetailFragment = RecipeDetailFragment.newInstance(recipe)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, recipeDetailFragment)
            .addToBackStack(null)
            .commit()
    }
}