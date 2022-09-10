package com.example.simpletodo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * A bridge that tells the recyclerview how to display the data we give it
 */
class TaskItemAdapter(val listOfItems: List<String> , val listOfStars: List<String> , val longClickListener: OnLongClickListener , val shortClickListener: OnShortClickListener , val starClickListener: OnStarClickListener) : RecyclerView.Adapter<TaskItemAdapter.ViewHolder>() {

    interface OnLongClickListener {
        fun onItemLongClicked(position: Int)
    }

    interface OnShortClickListener {
        fun onItemShortClicked(position: Int)
    }

    interface OnStarClickListener {
        fun onStarClicked(position: Int , isStarChecked: Boolean)
    }

    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.list_item_layout , parent , false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val item = listOfItems.get(position)

        holder.textView.text = item
        holder.starBox.isChecked = listOfStars.get(position).toBoolean()
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Store references to elements in our layout view
        val textView: TextView
        val starBox: CheckBox

        init {
            textView = itemView.findViewById(R.id.id_taskItem)
            starBox = itemView.findViewById(R.id.id_starBox)

            itemView.setOnLongClickListener {
                longClickListener.onItemLongClicked(adapterPosition)
                true
            }

            itemView.setOnClickListener {
                shortClickListener.onItemShortClicked(adapterPosition)
                true
            }

            starBox.setOnClickListener {
                starClickListener.onStarClicked(adapterPosition , starBox.isChecked)
            }
        }
    }

}