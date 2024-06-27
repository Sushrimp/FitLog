package edu.bluejack23_2.fitlog.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_2.fitlog.AddScheduleActivity
import edu.bluejack23_2.fitlog.R

class ItemAdapter(private val bodyPart : List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return bodyPart.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = bodyPart[position]
        holder.imageItem.setImageResource(currentItem.image)
        holder.titleItem.text = currentItem.title
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleItem : TextView = itemView.findViewById(R.id.textItem)
        val imageItem : ImageView = itemView.findViewById(R.id.imageItem)

    }
}

data class Item(val title: String, val image: Int)