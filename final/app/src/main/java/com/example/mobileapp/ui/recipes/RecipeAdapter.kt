package com.example.mobileapp.ui.recipes
import android.annotation.SuppressLint
import android.graphics.PorterDuff
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
import android.util.Base64
import com.bumptech.glide.Glide

class RecipeViewHolder(
    view: View,
    listener: RecipeAdapter.OnItemClickListener

) : RecyclerView.ViewHolder(view) {

    private val name : TextView = view.findViewById(R.id.name_recipe_list_item)
    private val ingredients : TextView = view.findViewById(R.id.ingredients_recipe_list_item)
    private val description : TextView = view.findViewById(R.id.description_recipe_list_item)
    private val picture : ImageView = view.findViewById(R.id.picture_recipe_list_item)
    private val star: ImageView = view.findViewById(R.id.star_recipe_list_item)
    private val cart: ImageView = view.findViewById(R.id.addToCart_recipe_list_item)


    init {
        //kogu elemendi kuulaja
        itemView.setOnClickListener { listener.onItemClick(adapterPosition) }
        star.setOnClickListener { listener.onStarClick(adapterPosition) }
        cart.setOnClickListener { listener.onCartClick(adapterPosition) }
    }
    //@SuppressLint("SetTextI18n")
    fun bind(recipeDto: RecipeDto){
        name.text = recipeDto.name
        description.text = recipeDto.description
        //ingredients.text = "Ingredients: ${recipeDto.ingredients?.joinToString()}"


        if(recipeDto.image.isNullOrEmpty()) picture.setImageResource(R.mipmap.ic_launcher)
        else {
            var image = recipeDto.image.split("base64")[1] // no need for fancy regex
            Glide.with(itemView.context)
                .asBitmap()
                .load(Base64.decode(image, Base64.DEFAULT))
                .placeholder(R.mipmap.ic_launcher)
                .into(picture)
        }

        if(recipeDto.fave){
            star.setImageResource(R.drawable.baseline_star_24)
            star.setColorFilter(ContextCompat.getColor(star.context, R.color.yellow), PorterDuff.Mode.SRC_IN)
        } else {
            star.setImageResource(R.drawable.baseline_star_border_24)
            star.setColorFilter(ContextCompat.getColor(star.context, R.color.black), PorterDuff.Mode.SRC_IN)
        }

        if(recipeDto.inCart){
            cart.setImageResource(R.drawable.baseline_shopping_cart_checkout_24)
            cart.setColorFilter(ContextCompat.getColor(star.context, R.color.purple_700), PorterDuff.Mode.SRC_IN)
        } else {
            cart.setImageResource(R.drawable.baseline_shopping_cart_24)
            cart.setColorFilter(ContextCompat.getColor(star.context, R.color.black), PorterDuff.Mode.SRC_IN)
        }
    }
}

class RecipeAdapter() : ListAdapter<RecipeDto, RecipeViewHolder>(DIFF_CONFIG) {

    private lateinit var onItemClickListener: OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onStarClick(position: Int)
        fun onCartClick(position: Int)
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