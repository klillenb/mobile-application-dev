package com.example.mobileapp.ui.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
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

        val removeRecipeFromCart = fun(id: String) {
            val recipe: RecipeDto? = homeViewModel.recipeList.value?.find {
                it._id == id
            }
            val pos: Int? = homeViewModel.recipeList.value?.indexOf(recipe)
            homeViewModel.removeFromCart(pos!!)
        }

        homeViewModel.recipeList.value?.forEach {
            recipe: RecipeDto ->
                if (recipe.inCart) {
                    recipe.ingredients.forEach {
                        ingredient: String ->
                            items.add(ShoppingCartItem(ingredient, recipe._id))
                    }
                }
        }

        shoppingCartItemViewAdapter = ShoppingCartItemViewAdapter(items, removeRecipeFromCart)
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
}