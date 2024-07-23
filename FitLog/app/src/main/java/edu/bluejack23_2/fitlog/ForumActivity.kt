package edu.bluejack23_2.fitlog

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack23_2.fitlog.databinding.ActivityForumBinding
import edu.bluejack23_2.fitlog.databinding.ActivityHomeBinding
import edu.bluejack23_2.fitlog.handler.ForumHandler
import edu.bluejack23_2.fitlog.handler.UserHandler
import edu.bluejack23_2.fitlog.models.Forum
import edu.bluejack23_2.fitlog.models.ForumAdapter
import edu.bluejack23_2.fitlog.models.ReplyAdapter

class ForumActivity : AppCompatActivity() {
    private lateinit var forumDetails : Forum

    private lateinit var userHandler : UserHandler
    private lateinit var forumHandler : ForumHandler

    private lateinit var binding : ActivityForumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forum)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        forumHandler = ForumHandler()
        userHandler = UserHandler()

        // Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Forum Details"

        val forumId = intent.getStringExtra("forumId").toString()
        if (forumId != null) {
            setForumDetails(forumId)
        } else {
            binding.name.text = "Error"
            binding.username.text = "Error"
            binding.forumContent.text = "Error"
        }
        setSendReply()
    }

    private fun setForumDetails(forumId: String) {
        forumHandler.getForum(forumId) { forum ->
            if (forum != null) {
                forum.posterUid?.let { userHandler.setProfilePictureById(it, binding.profilePicture) }
                binding.name.text = forum.name
                binding.username.text = forum.username
                binding.forumContent.text = forum.content
                forumDetails = forum

                val replies = mutableListOf<String>()
                forum.replies?.let {
                    replies.addAll(it)
                }
                val adapter = ReplyAdapter(replies)
                binding.replies.adapter = adapter
                binding.replies.layoutManager = LinearLayoutManager(this)
            } else {
                binding.name.text = "Error"
                binding.username.text = "Error"
                binding.forumContent.text = "Error"
            }
        }
    }

    private fun setSendReply() {
        binding.sendReplyButton.setOnClickListener{
            val reply = binding.replyField.text.toString()

            forumHandler.addReply(forumDetails.forumId, reply) { response ->
                Toast.makeText(this, response.msg, Toast.LENGTH_SHORT).show()
                if(response.status){
                    binding.replyField.text.clear()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}