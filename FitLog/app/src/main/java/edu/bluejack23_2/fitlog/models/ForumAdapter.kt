package edu.bluejack23_2.fitlog.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_2.fitlog.R

class ForumAdapter(private val forums: List<Forum>) : RecyclerView.Adapter<ForumAdapter.ForumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_forum, parent, false)
        return ForumViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return forums.size
    }

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        val currentItem = forums[position]
        holder.titleItem.text = currentItem.uid
    }

    class ForumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleItem: TextView = itemView.findViewById(R.id.name)
    }
}

data class Forum(val uid: String)