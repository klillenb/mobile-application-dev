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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val homeViewModel =
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
            override fun onItemClick(position: Int, view: View) {
                //Toast.makeText(activity, "klikkisid kogu elemendile", Toast.LENGTH_SHORT).show()
                val recipe = recipeAdapter.currentList[position]
                //showRecipeDetail(recipe)
                //recipeAdapter.notifyItemChanged(position)

                val action = RecipesFragmentDirections.actionNavigationRecipesToRecipeDetailFragment(argRecipeDetail = recipe)
                view.findNavController().navigate(action)
            }

            override fun onStarClick(position: Int) {
                //Toast.makeText(activity, "klikkisid tähele", Toast.LENGTH_SHORT).show()
                homeViewModel.toggleFave(position)
                recipeAdapter.notifyItemChanged(position)
            }

            override fun onCartClick(position: Int) {
                //Toast.makeText(activity, "klikkisid kärule", Toast.LENGTH_SHORT).show()
                homeViewModel.toggleAddToCart(position)
                recipeAdapter.notifyItemChanged(position)
            }
        })

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
/*    private fun showRecipeDetail(recipe: RecipeDto) {
        val recipeDetailFragment = RecipeDetailFragment.newInstance(recipe)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, recipeDetailFragment)
            .addToBackStack(null)
            .commit()
    }*/
}