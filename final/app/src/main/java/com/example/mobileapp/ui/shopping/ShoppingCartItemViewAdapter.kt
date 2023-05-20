package com.example.mobileapp.ui.shopping

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import com.example.mobileapp.R
import android.view.ViewGroup
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.mobileapp.dto.ShoppingCartDto
import com.example.mobileapp.model.SharedViewModel

/**
 * Responsible for generating shopping list display
 */

class ShoppingCartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val checkBox: CheckBox = itemView.findViewById(R.id.checkBox_shopping_item)
    val textView: TextView = itemView.findViewById(R.id.textView_shopping_item)
    val button: ImageButton = itemView.findViewById(R.id.button_shopping_item)

}
class ShoppingCartItemViewAdapter(
    private val viewModel: SharedViewModel
    ): ListAdapter<ShoppingCartDto, ShoppingCartViewHolder>(DIFF_CONFIG) {

    companion object{
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<ShoppingCartDto>(){
            override fun areItemsTheSame(oldItem: ShoppingCartDto, newItem: ShoppingCartDto): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: ShoppingCartDto,
                newItem: ShoppingCartDto
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder {
        return ShoppingCartViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_shopping_cart,
                parent,
                false
            )
        )
    }
//  override fun getItemCount(): Int {
//        return viewModel.shoppingCartItems.value?.size ?: 0
//    }
//

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        val item: ShoppingCartDto = viewModel.shoppingCartItems.value!![position]
        holder.textView.text = item.name
        holder.checkBox.isChecked = item.done
        holder.checkBox.setOnCheckedChangeListener {
            _: CompoundButton, checked: Boolean ->
                //items[position].done = checked
                viewModel.markChecked(item, checked)
                //viewModel.removeFromShoppingCart(position)
                //notifyItemChanged(position)
        }
        holder.button.setOnClickListener {
            //items.removeAt(position)
            //val recipe = viewModel.recipeList?.value.find { it._id == item.recipeId }
            viewModel.removeIngredientFromShoppingCart(item)
            //notifyItemRemoved(position)
            //notifyItemRangeChanged(position, items.size)
        }
    }
}

