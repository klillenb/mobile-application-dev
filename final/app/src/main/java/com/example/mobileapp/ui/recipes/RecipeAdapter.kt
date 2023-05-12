package com.example.mobileapp.ui.recipes


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.dto.RecipeDto
import kotlin.text.Typography.ellipsis


class RecipeViewHolder(
    view: View,
) : RecyclerView.ViewHolder(view) {

    private val name : TextView = view.findViewById(R.id.name_recipe_list_item)
    private val ingredients : TextView = view.findViewById(R.id.ingredients_recipe_list_item)
    private val description : TextView = view.findViewById(R.id.description_recipe_list_item)
    private val picture : ImageView = view.findViewById(R.id.picture_recipe_list_item)

    @SuppressLint("SetTextI18n")
    fun bind(recipeDto: RecipeDto){
        name.text = recipeDto.name
        description.text = recipeDto.description
        ingredients.text = "Ingredients: ${recipeDto.ingredients?.joinToString()}"

        picture.setImageResource(R.mipmap.ic_launcher)

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