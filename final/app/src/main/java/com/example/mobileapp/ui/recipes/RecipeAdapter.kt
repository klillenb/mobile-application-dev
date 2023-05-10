package com.example.mobileapp.ui.recipes

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.dto.RecipeDto

class RecipeViewHolder(
    view: View,
) : RecyclerView.ViewHolder(view) {

    private val name : TextView = view.findViewById(R.id.name_recipe_list_item)
    private val ingredients : TextView = view.findViewById(R.id.ingredients_recipe_list_item)
    private val description : TextView = view.findViewById(R.id.description_recipe_list_item)
    private val picture : ImageView = view.findViewById(R.id.picture_recipe_list_item)
    private val star: ImageView = view.findViewById(R.id.star_recipe_list_item)

    fun bind(recipeDto: RecipeDto){
        name.text = recipeDto.name
        description.text = recipeDto.description
        ingredients.text = "Ingredients: ${recipeDto.ingredients.joinToString()}"

        picture.setImageResource(R.mipmap.ic_launcher)
        //star.setColorFilter(R.color.yellow)
        if(recipeDto.fave){
        //if(recipeDto._id == "638f9e3e8f6e745237a80114"){
            Log.d("STATUS", "Inf jõuab kohale adapterisse: $recipeDto")
            star.setImageResource(R.drawable.baseline_star_24)
            star.setColorFilter(ContextCompat.getColor(star.context, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }
}


class RecipeAdapter(
    private val clickHandler: (RecipeDto) -> Unit
) : ListAdapter<RecipeDto, RecipeViewHolder>(DIFF_CONFIG) {

    companion object{
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<RecipeDto>(){
            override fun areItemsTheSame(oldItem: RecipeDto, newItem: RecipeDto): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: RecipeDto,
                newItem: RecipeDto
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe_list, parent, false)
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            clickHandler(getItem(position))
        }
    }
}