package com.example.mobileapp.ui.recipes


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.repository.Recipe



class RecipeViewHolder(
    view: View,
) : RecyclerView.ViewHolder(view) {

    private val name : TextView = view.findViewById(R.id.name_recipe_list_item)
    private val description : TextView = view.findViewById(R.id.description_recipe_list_item)

    fun bind(recipe: Recipe){
        name.text = recipe.name
        description.text = recipe.description
    }
}


class RecipeAdapter(
    private val clickHandler: (Recipe) -> Unit
) : ListAdapter<Recipe, RecipeViewHolder>(DIFF_CONFIG) {

    companion object{
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<Recipe>(){
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Recipe,
                newItem: Recipe
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