package com.example.mobileapp.ui.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.databinding.FragmentShoppingBinding

class ShoppingFragment : Fragment() {

    private var _binding: FragmentShoppingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var shoppingCartItemViewAdapter: ShoppingCartItemViewAdapter
    private lateinit var items: MutableList<ShoppingCartItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        items = mutableListOf()

        for (i in 0..25) {
            items.add(ShoppingCartItem("Hello world #"))
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
}