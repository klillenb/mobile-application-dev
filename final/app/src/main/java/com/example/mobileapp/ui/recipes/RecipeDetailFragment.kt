package com.example.mobileapp.ui.recipes

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobileapp.R
import com.example.mobileapp.dto.RecipeDto
import com.example.mobileapp.databinding.FragmentRecipeDetailBinding
import com.bumptech.glide.Glide

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipe: RecipeDto


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        arguments?.let {
            recipe = it.getParcelable("arg_recipe_detail")!!
        }


        //set image
            if(recipe.image.isNullOrEmpty()) binding.imageviewRecipe.setImageResource(R.mipmap.ic_launcher)
            else {
                val image = recipe.image
                Glide.with(binding.imageviewRecipe.context)
                    .asBitmap()
                    .load(Base64.decode(image, Base64.DEFAULT))
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.imageviewRecipe)
            }

        binding.textviewTitle.text = recipe.name
        binding.textviewIngredients.text = recipe.ingredients?.joinToString("\n")
        binding.textviewInstructions.text = recipe.instructions
        binding.textviewdescription.text = recipe.description

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

/*    companion object {
        fun newInstance(recipe: RecipeDto?): RecipeDetailFragment {
            val fragment = RecipeDetailFragment()
            val args = Bundle()
            args.putParcelable("recipe", recipe)
            fragment.arguments = args
            return fragment
        }
    }*/

}
