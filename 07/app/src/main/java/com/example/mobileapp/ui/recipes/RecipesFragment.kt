package com.example.mobileapp.ui.recipes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapp.databinding.FragmentRecipesBinding

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private lateinit var safeContext: Context

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recipesViewModel =
            ViewModelProvider(this).get(RecipesViewModel::class.java)

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btButton.setOnClickListener {
            if (binding.foodText.text.isNotEmpty()) {
                recipesViewModel.findNutrition(binding.foodText.text.toString())
            } else {
                Toast.makeText(safeContext, "Insert a food item first", Toast.LENGTH_SHORT).show()
            }
        }

        recipesViewModel._status.observe(viewLifecycleOwner, Observer { _status ->
            _status?.let {
                if(!_status) {
                    recipesViewModel._status.value = null
                    Toast.makeText(safeContext, "Something went wrong!", Toast.LENGTH_SHORT).show()
                }
            }
        })

        recipesViewModel.nutrition.observe(viewLifecycleOwner) {
            binding.foodName.text = "Item name: ${it.name}"
            binding.foodCalories.text = "Calorie amount: ${it.calories}"
            binding.foodSize.text = "Serving size: ${it.servingSize}"
            binding.foodFat.text = "Fat amount: ${it.fatTotal}"
            binding.foodProtein.text = "Protein amount: ${it.protein}"
            binding.foodSugar.text = "Sugar amount: ${it.sugar}"
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}