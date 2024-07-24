package edu.bluejack23_2.fitlog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack23_2.fitlog.databinding.ActivityProfileBinding
import edu.bluejack23_2.fitlog.handler.AuthenticationHandler
import edu.bluejack23_2.fitlog.handler.UserHandler

class ProfileActivity : AppCompatActivity() {
    private lateinit var authHandler: AuthenticationHandler
    private lateinit var userHandler: UserHandler

    private lateinit var signOutButton : Button

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Profile"

        userHandler = UserHandler()
        authHandler = AuthenticationHandler()
        signOutButton = findViewById(R.id.signOut_Profile)

        signOutButton.setOnClickListener{
            handleSignOut()
        }

        setDetails()

        binding.editProfileProfile.setOnClickListener{
            val showForm = EditProfileFragment()
            showForm.show(supportFragmentManager, "showForm")
        }
        binding.changePasswordProfile.setOnClickListener{
            val showForm = ChangePasswordFragment()
            showForm.show(supportFragmentManager, "showForm")
        }
    }

    private fun handleSignOut (){
        authHandler.signOut { }
        val intent = Intent(this@ProfileActivity, OnboardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setDetails(){
        userHandler.getUserDetails { response ->
            if(response.status){
                findViewById<TextView>(R.id.name_Profile).text = response.user.name
                findViewById<TextView>(R.id.username_Profile).text = "@" + response.user.username
                findViewById<TextView>(R.id.email_Profile).text = response.user.email
                findViewById<TextView>(R.id.gender_Profile).text = response.user.gender
                findViewById<TextView>(R.id.age_Profile).text = "Age : " + response.user.age
            }
        }
        userHandler.setProfilePicture(binding.profilePictureProfile)
    }

    override fun onResume() {
        super.onResume()
        setDetails()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}