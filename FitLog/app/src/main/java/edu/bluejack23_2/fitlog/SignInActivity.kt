package edu.bluejack23_2.fitlog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack23_2.fitlog.handler.AuthenticationHandler

class SignInActivity : AppCompatActivity() {
    private lateinit var authHandler : AuthenticationHandler

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var errorText: TextView
    private lateinit var signInBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        authHandler = AuthenticationHandler()
        // Take used components
        emailField = findViewById(R.id.email_signIn)
        passwordField = findViewById(R.id.password_signIn)
        errorText = findViewById(R.id.errorTxt_signIn)
        signInBtn = findViewById(R.id.signIn_signIn)

        signInBtn.setOnClickListener {
            handleSignIn()
        }
    }

    private fun handleSignIn(){
        val email = emailField.text.toString()
        val password = passwordField.text.toString()

        authHandler.signIn(email, password){response ->
            if(!response.status){
                errorText.text = response.msg
            }
            else {
                errorText.text = ""

                Toast.makeText(this, "Sign in success !", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 1000)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}