package edu.bluejack23_2.fitlog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    internal lateinit var emailField: EditText
    internal lateinit var passwordField: EditText
    internal lateinit var errorText: TextView
    internal lateinit var signInBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        auth = FirebaseAuth.getInstance()
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

        if(email.equals("")|| password.equals("")){
            errorText.text = "All field must not be empty"
            return
        }
        errorText.text = ""

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    val user = auth.currentUser
//                    errorText.text = user?.uid.toString()

//                    val intent = Intent(this@SignInActivity, ProfileActivity::class.java)
                    val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    errorText.text = "Invalid Credentials"
                }
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}