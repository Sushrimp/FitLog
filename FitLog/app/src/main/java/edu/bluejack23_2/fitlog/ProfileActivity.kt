package edu.bluejack23_2.fitlog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var signOutButton : Button

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile)

        signOutButton = findViewById(R.id.signOut_profile)

        signOutButton.setOnClickListener{
            handleSignOut()
        }

        auth = FirebaseAuth.getInstance()
        val textV = findViewById<TextView>(R.id.textView)
        textV.text = auth.currentUser.toString()

    }

    private fun  handleSignOut (){
        auth.signOut()

        val text = findViewById<TextView>(R.id.textView)
        text.text = auth.currentUser.toString()
    }
}