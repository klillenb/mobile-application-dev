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

    class ShoppingData(val items: ArrayList<String>, val checked: ArrayList<Int>)
    private val dataset: ShoppingData = ShoppingData(
        ArrayList(),
        ArrayList()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* val homeViewModel =
            ViewModelProvider(this)[ShoppingViewModel::class.java]
        */

        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.recyclerView
        binding.completeAllButton.setOnClickListener {
            dataset.checked.clear()
            for (i in 0..dataset.items.size) {
                dataset.checked.add(i)
            }
        }
        recyclerView.adapter = CartItemViewAdapter(
            dataset
        )
        // vist pole õige
        recyclerView.layoutManager = LinearLayoutManager(this.activity)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}