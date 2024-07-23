package edu.bluejack23_2.fitlog.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import edu.bluejack23_2.fitlog.R
import edu.bluejack23_2.fitlog.handler.UserHandler

class ForumAdapter(
    private val forums: List<Forum>,
    private val onForumClickListener: OnForumClickListener
) : RecyclerView.Adapter<ForumAdapter.ForumViewHolder>() {



    interface OnForumClickListener {
        fun onForumClick(forumId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_forum, parent, false)
        return ForumViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return forums.size
    }

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        val currentItem = forums[position]

        var userHandler : UserHandler
        userHandler = UserHandler()

        currentItem.posterUid?.let { userHandler.setProfilePictureById(it, holder.image) }
        holder.name.text = currentItem.name
        holder.username.text = currentItem.username
        holder.content.text = currentItem.content

        holder.itemView.setOnClickListener {
            onForumClickListener.onForumClick(currentItem.forumId)
        }
    }

    class ForumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val username: TextView = itemView.findViewById(R.id.username)
        val content: TextView = itemView.findViewById(R.id.forumContent)
        val image: ImageView = itemView.findViewById(R.id.profilePicture)
    }
}

data class Forum(
    val forumId: String,
    val posterUid: String?,
    val name: String?,
    val username: String?,
    val content: String?,
    val replies: List<String>? = null,
    val timestamp: Timestamp? = null
)