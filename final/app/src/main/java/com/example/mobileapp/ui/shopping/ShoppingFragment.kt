package com.example.mobileapp.ui.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.databinding.FragmentShoppingBinding
import com.example.mobileapp.dto.ShoppingCartDto
import com.example.mobileapp.model.SharedViewModel

class ShoppingFragment : Fragment() {

    private var _binding: FragmentShoppingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val viewModel = ViewModelProvider(this)[sharedViewModel::class.java]
        val viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]


        val shoppingCartItemViewAdapter = ShoppingCartItemViewAdapter(
            viewModel
        )
        //viewModel.setShoppingCartItemAdapter(shoppingCartItemViewAdapter)

        val recyclerView: RecyclerView = binding.recyclerViewShopping
        recyclerView.adapter = shoppingCartItemViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val shoppingCartObserver = Observer<List<ShoppingCartDto>>{ shoppingCartItem ->
            shoppingCartItemViewAdapter.submitList(shoppingCartItem)
        }
        viewModel.shoppingCartItems.observe(viewLifecycleOwner, shoppingCartObserver)

        binding.buttonShoppingComplete.setOnClickListener {
            viewModel.completeAllItems()
            viewModel.shoppingCartItems.value?.let { it1 -> shoppingCartItemViewAdapter.notifyItemRangeChanged(0, it1.size) }
        }

        binding.buttonShoppingDelete.setOnClickListener {
            viewModel.deleteDoneItems()
            //viewModel.shoppingCartItems.value?.let { it1 -> shoppingCartItemViewAdapter.notifyItemRangeChanged(0, it1.size) }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}