package edu.bluejack23_2.fitlog.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_2.fitlog.R
import edu.bluejack23_2.fitlog.handler.UserHandler

class ReplyAdapter(private val replies : List<String>) : RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_reply, parent, false)
        return ReplyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return replies.size
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        val currentItem = replies[position]
        holder.reply.text = currentItem.toString()
    }

    class ReplyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reply: TextView = itemView.findViewById(R.id.replyContent)
    }
}