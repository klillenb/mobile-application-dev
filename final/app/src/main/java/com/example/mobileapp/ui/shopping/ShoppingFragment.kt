package com.example.mobileapp.ui.shopping

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.databinding.FragmentShoppingBinding
import com.example.mobileapp.dto.RecipeDto
import com.example.mobileapp.model.SharedViewModel

class ShoppingFragment : Fragment() {

    private var _binding: FragmentShoppingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var shoppingCartItemViewAdapter: ShoppingCartItemViewAdapter
    private lateinit var items: MutableList<ShoppingCartItem>

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val homeViewModel =
            ViewModelProvider(this)[sharedViewModel::class.java]
        items = mutableListOf()

        homeViewModel.recipeList.value?.forEach {
            recipe: RecipeDto ->
                Log.d(TAG, "${recipe.name}, ${recipe.inCart}")
                if (recipe.inCart) {
                    recipe.ingredients.forEach {
                        items.add(ShoppingCartItem(it))
                    }
                }
        }

        shoppingCartItemViewAdapter = ShoppingCartItemViewAdapter(items)
        val recyclerView: RecyclerView = binding.recyclerViewShopping
        recyclerView.adapter = shoppingCartItemViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        binding.buttonShoppingComplete.setOnClickListener {
            shoppingCartItemViewAdapter.completeAllItems()
        }

        binding.buttonShoppingDelete.setOnClickListener {
            shoppingCartItemViewAdapter.deleteDoneItems()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

    }
}