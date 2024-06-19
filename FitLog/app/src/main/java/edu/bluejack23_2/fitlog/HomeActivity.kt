package edu.bluejack23_2.fitlog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import edu.bluejack23_2.fitlog.handler.UserHandler

class HomeActivity : AppCompatActivity() {

    private lateinit var profilePicture : ImageView
    private lateinit var userHandler : UserHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        userHandler = UserHandler()

        profilePicture = findViewById(R.id.profilePicture_Home)
        userHandler.setProfilePicture(profilePicture)

        profilePicture.setOnClickListener{
            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

}