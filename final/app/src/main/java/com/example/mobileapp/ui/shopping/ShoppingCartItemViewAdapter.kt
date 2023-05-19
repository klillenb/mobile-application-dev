package com.example.mobileapp.ui.shopping

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import com.example.mobileapp.R
import android.view.ViewGroup
import android.view.View
import android.widget.*
import com.example.mobileapp.dto.ShoppingCartDto

/**
 * Responsible for generating shopping list display
 */
class ShoppingCartItemViewAdapter(
    private val items: MutableList<ShoppingCartDto>,
    private val removeFromShoppingList: (pos: Int) -> Unit
    ): RecyclerView.Adapter<ShoppingCartItemViewAdapter.ShoppingCartViewHolder>() {

    class ShoppingCartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox
        val textView: TextView
        val button: ImageButton

        init {
            checkBox = itemView.findViewById(R.id.checkBox_shopping_item)
            textView = itemView.findViewById(R.id.textView_shopping_item)
            button = itemView.findViewById(R.id.button_shopping_item)
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

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        val item: ShoppingCartDto = items[position]
        holder.textView.text = item.name
        holder.checkBox.isChecked = item.done
        holder.checkBox.setOnCheckedChangeListener {
            // Lambda function!
            _: CompoundButton, checked: Boolean ->
                items[position].done = checked
                removeFromShoppingList(position)
                notifyItemChanged(position)
        }
        holder.button.setOnClickListener {
            items.removeAt(position)
            removeFromShoppingList(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }
    }

    fun completeAllItems() {
        items.forEach {
            item: ShoppingCartDto ->
                item.done = true
        }
        notifyItemRangeChanged(0, items.size)
    }

    fun deleteDoneItems() {
        items.removeIf {
            item: ShoppingCartDto ->
                item.done
        }
        notifyDataSetChanged()
    }
}

