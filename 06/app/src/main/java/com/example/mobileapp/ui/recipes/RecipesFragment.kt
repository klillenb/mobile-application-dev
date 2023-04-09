package com.example.mobileapp.ui.recipes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
        val homeViewModel =
            ViewModelProvider(this).get(RecipesViewModel::class.java)

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadData()

        binding.btButton.setOnClickListener { saveData() }
        return root
    }

    private fun saveData() {
        val inputText = binding.editText
        val textField = binding.textRecipes
        val switch = binding.swSwitch
        val insertedText = inputText.text.toString()
        textField.text = insertedText

        val sharedPreferences = this.activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.apply{
            putString("STRING_KEY", insertedText)
            putBoolean("BOOLEAN_KEY", switch.isChecked)
        }?.apply()

        Toast.makeText(safeContext, "Data saved", Toast.LENGTH_SHORT).show()

    }

    private fun loadData() {
        val textField = binding.textRecipes
        val switch = binding.swSwitch

        val sharedPreferences = this.activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedString = sharedPreferences?.getString("STRING_KEY", null)
        val savedBoolean = sharedPreferences?.getBoolean("BOOLEAN_KEY", false)

        textField.text = savedString
        if (savedBoolean != null) {
            switch.isChecked = savedBoolean
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}