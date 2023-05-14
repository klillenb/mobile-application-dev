package com.example.mobileapp.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentRecipesBinding
import com.example.mobileapp.dto.RecipeDto
import com.example.mobileapp.model.SharedViewModel

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val recipeViewModel =
            ViewModelProvider(this)[sharedViewModel::class.java]

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.recyclerViewRecipes
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val recipeAdapter = RecipeAdapter()

        /*val recipeAdapter = RecipeAdapter(){recipe ->
            //val msg = getString(R.string.forecast_clicked_format, forecastItem.temp, forecastItem.description)
            showRecipeDetail(recipe)
            //showForecastDetails(forecast)
        }*/


        recipeAdapter.setOnItemClickListener(object : RecipeAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                //Toast.makeText(activity, "klikkisid kogu elemendile", Toast.LENGTH_SHORT).show()
                val recipe = recipeAdapter.currentList[position]
                showRecipeDetail(recipe)

            }

            override fun onStarClick(position: Int) {
                recipeViewModel.toggleFave(position)
                recipeAdapter.notifyItemChanged(position)
            }

            override fun onCartClick(position: Int) {
                recipeViewModel.toggleAddToCart(position)
                recipeAdapter.notifyItemChanged(position)
            }
        })

        recyclerView.adapter = recipeAdapter

        val recipeDtoObserver = Observer<List<RecipeDto>>{ recipes ->
            recipeAdapter.submitList(recipes)
        }
        recipeViewModel.recipeList.observe(viewLifecycleOwner, recipeDtoObserver )
        recipeViewModel.showProgress.observe(viewLifecycleOwner, Observer{
            binding.recipeProgressBar.visibility = if(it) View.VISIBLE else View.GONE
        })

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