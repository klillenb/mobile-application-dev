package com.example.mobileapp.ui.recipes

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
    listener: RecipeAdapter.OnItemClickListener

) : RecyclerView.ViewHolder(view) {

    private val name : TextView = view.findViewById(R.id.name_recipe_list_item)
    private val ingredients : TextView = view.findViewById(R.id.ingredients_recipe_list_item)
    private val description : TextView = view.findViewById(R.id.description_recipe_list_item)
    private val picture : ImageView = view.findViewById(R.id.picture_recipe_list_item)
    private val star: ImageView = view.findViewById(R.id.star_recipe_list_item)

    init {
        itemView.setOnClickListener { listener.onItemClick(itemView, adapterPosition) }
        star.setOnClickListener { listener.onStarClick(star, adapterPosition) }
        picture.setOnClickListener { listener.onPictureClick(picture, adapterPosition) }
    }
    fun bind(recipeDto: RecipeDto){
        name.text = recipeDto.name
        description.text = recipeDto.description
        ingredients.text = "Ingredients: ${recipeDto.ingredients.joinToString()}"

        picture.setImageResource(R.mipmap.ic_launcher)
        if(recipeDto.fave){
            star.setImageResource(R.drawable.baseline_star_24)
            star.setColorFilter(ContextCompat.getColor(star.context, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }
}

class RecipeAdapter : ListAdapter<RecipeDto, RecipeViewHolder>(DIFF_CONFIG) {

    private lateinit var onItemClickListener: OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onStarClick(view: View, position: Int)
        fun onPictureClick(view: View, position: Int)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        onItemClickListener = listener
    }
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
        return RecipeViewHolder(itemView, onItemClickListener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}