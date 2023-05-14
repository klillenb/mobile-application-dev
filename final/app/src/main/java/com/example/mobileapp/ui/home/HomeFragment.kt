package com.example.mobileapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapp.databinding.FragmentHomeBinding
import com.example.mobileapp.model.SharedViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[sharedViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val quoteTextView: TextView = binding.quote
        val authorTextView: TextView = binding.author

        homeViewModel.quote.observe(viewLifecycleOwner) {
            quoteTextView.text = it.quote
            authorTextView.text = it.author
        }

        homeViewModel.showProgress.observe(viewLifecycleOwner, Observer{
            binding.progressBar.visibility = if(it) View.VISIBLE else View.GONE
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}