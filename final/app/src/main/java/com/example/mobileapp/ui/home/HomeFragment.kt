package com.example.mobileapp.ui.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentHomeBinding
import com.example.mobileapp.model.SharedViewModel
import org.w3c.dom.Text
import com.bumptech.glide.Glide
import kotlin.random.Random

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val homeViewModel = ViewModelProvider(this)[sharedViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val quoteTextView: TextView = binding.quote
        val authorTextView: TextView = binding.author
        val recipeOfTheDayPic: ImageView = binding.recipeOfDayPic
        val recipeOfTheDayText: TextView = binding.recipeOfDayText


        //kodulehel tsitaadi kuvamine
        homeViewModel.quote.observe(viewLifecycleOwner) {
            quoteTextView.text = it.quote
            authorTextView.text = it.author
        }

        homeViewModel.showProgress.observe(viewLifecycleOwner, Observer{
            binding.progressBar.visibility = if(it) View.VISIBLE else View.GONE
        })

        //kodulehel pärva retsepti kuvamine
        homeViewModel.recipeList.observe(viewLifecycleOwner) {
            val randomNr = Random.nextInt(0,it.size);
            recipeOfTheDayText.text = it[randomNr].name
            if(it[randomNr].image.isNullOrEmpty()) recipeOfTheDayPic.setImageResource(R.mipmap.ic_launcher)
            else {
                var image = it[randomNr].image
                Glide.with(recipeOfTheDayPic.context)
                    .asBitmap()
                    .load(Base64.decode(image, Base64.DEFAULT))
                    .placeholder(R.mipmap.ic_launcher)
                    .into(recipeOfTheDayPic)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}