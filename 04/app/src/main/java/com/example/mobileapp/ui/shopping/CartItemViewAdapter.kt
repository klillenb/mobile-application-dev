package com.example.mobileapp.ui.shopping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R

class CartItemViewAdapter(private val dataset: ShoppingFragment.ShoppingData) :
    RecyclerView.Adapter<CartItemViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val deleteButton: ImageButton
        val checkbox: CheckBox

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.text_view)
            deleteButton = view.findViewById(R.id.delete_button)
            checkbox = view.findViewById(R.id.checkbox)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.shopping_cart_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataset.items[position]
        viewHolder.deleteButton.setOnClickListener {
            dataset.items.removeAt(position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.items.size

}

