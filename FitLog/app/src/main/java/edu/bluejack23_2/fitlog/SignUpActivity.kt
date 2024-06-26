package edu.bluejack23_2.fitlog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack23_2.fitlog.handler.AuthenticationHandler
import edu.bluejack23_2.fitlog.models.Response
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var  authHandler : AuthenticationHandler

    private lateinit var nameField : EditText
    private lateinit var usernameField : EditText
    private lateinit var ageField : EditText
    private lateinit var genderGroup : RadioGroup
    private lateinit var emailField : EditText
    private lateinit var passwordField : EditText
    private lateinit var confirmPasswordField : EditText
    private lateinit var signUpBtn : Button

    private lateinit var errorTxt : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        authHandler = AuthenticationHandler()
        // set variable
        nameField = findViewById(R.id.name_signUp)
        usernameField = findViewById(R.id.username_signUp)
        ageField = findViewById(R.id.age_signUp)
        genderGroup = findViewById(R.id.radioGroup_signUp)
        emailField = findViewById(R.id.email_signUp)
        passwordField = findViewById(R.id.password_signUp)
        confirmPasswordField = findViewById(R.id.confirmPassword_signUp)
        signUpBtn = findViewById(R.id.signUp_signUp)
        errorTxt = findViewById(R.id.errorTxt_signUp)


        signUpBtn.setOnClickListener{
            handleSignUp()
        }

    }

    private fun handleSignUp(){
        val name = nameField.text.toString()
        val username = usernameField.text.toString()
        val age = ageField.text.toString()
        val email = emailField.text.toString()
        val password = passwordField.text.toString()
        val confirmPassword = confirmPasswordField.text.toString()
        val selectedRadioButtonId = genderGroup.checkedRadioButtonId

        if(name.equals("") || username.equals("") || age.equals("")
            || email.equals("") || password.equals("") || confirmPassword.equals("")
            || selectedRadioButtonId == -1){
            errorTxt.text = "All fields must be filled"
            return
        }

        val genderSelected = findViewById<RadioButton>(selectedRadioButtonId)

        authHandler.signUp(name, username, age, email, password, confirmPassword, genderGroup, genderSelected){response ->
            if(!response.status){
                errorTxt.text = response.msg
            }
            else {
                errorTxt.text = ""

                Toast.makeText(this, "Sign up success!", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@SignUpActivity, ProfileActivity::class.java)
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