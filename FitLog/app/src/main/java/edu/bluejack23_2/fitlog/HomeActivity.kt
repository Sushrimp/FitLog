package edu.bluejack23_2.fitlog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import edu.bluejack23_2.fitlog.handler.HomeHandler

class HomeActivity : AppCompatActivity() {

    private lateinit var profilePicture : ImageView
    private lateinit var homeHandler : HomeHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        homeHandler = HomeHandler()

        profilePicture = findViewById(R.id.profilePicture_Home)

        homeHandler.setProfilePicture(profilePicture)
    }
}