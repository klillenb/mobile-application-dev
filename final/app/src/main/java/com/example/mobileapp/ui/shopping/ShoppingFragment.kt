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
import com.example.mobileapp.dto.ShoppingCartDto
import com.example.mobileapp.model.SharedViewModel

class ShoppingFragment : Fragment() {

    private var _binding: FragmentShoppingBinding? = null

    private val binding get() = _binding!!

    private lateinit var shoppingCartItemViewAdapter: ShoppingCartItemViewAdapter
    private lateinit var items: MutableList<ShoppingCartDto>

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModel = ViewModelProvider(this)[sharedViewModel::class.java]

        val removeFromShoppingCart: (pos: Int) -> Unit = fun(pos: Int) {
            viewModel.toggleAddToCart(pos, true)
        }

        items = mutableListOf()

        viewModel.recipeList.value?.forEach {
            recipe: RecipeDto ->
                if (recipe.inCart) {
                    val index: Int = viewModel.recipeList.value?.indexOf(recipe)!!
                    recipe.ingredients.forEach {
                        ingredient: String ->
                            items.add(ShoppingCartDto(
                                ingredient,
                                index
                            ))
                    }
                }
        }

        shoppingCartItemViewAdapter = ShoppingCartItemViewAdapter(
            items,
            removeFromShoppingCart
        )
        val recyclerView: RecyclerView = binding.recyclerViewShopping
        recyclerView.adapter = shoppingCartItemViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        binding.buttonShoppingComplete.setOnClickListener {
            shoppingCartItemViewAdapter.completeAllItems()
            items.forEach {
                removeFromShoppingCart(it.recipeIndex)
            }
        }

        binding.buttonShoppingDelete.setOnClickListener {
            shoppingCartItemViewAdapter.deleteDoneItems()
            items.forEach {
                removeFromShoppingCart(it.recipeIndex)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}