package edu.bluejack23_2.fitlog

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OnboardingActivity : AppCompatActivity() {

    private lateinit var signInBtn : Button
    private lateinit var signUpBtn : Button
    private lateinit var signGuestBtn : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_onboarding)

        signInBtn = findViewById(R.id.signIn_Onboarding)
        signUpBtn = findViewById(R.id.signUp_Onboarding)
        signGuestBtn = findViewById(R.id.signGuest_Onboarding)
        setButtonListener()

    }

    private fun setButtonListener(){
        signInBtn.setOnClickListener{
            val intent = Intent(this@OnboardingActivity, SignInActivity::class.java)
            startActivity(intent)

        }
        signUpBtn.setOnClickListener{
            val intent = Intent(this@OnboardingActivity, SignUpActivity::class.java)
            startActivity(intent)

        }
    }

}