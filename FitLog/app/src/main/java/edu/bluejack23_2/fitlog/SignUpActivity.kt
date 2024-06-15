package edu.bluejack23_2.fitlog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var  db : FirebaseFirestore
    private lateinit var  auth : FirebaseAuth

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

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

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
            errorTxt.text = "All field must be filled"
            return
        }
        else if(password.length < 8){
            errorTxt.text = "Password length must be more than 8 characters"
            return
        }
        else if (password != confirmPassword) {
            errorTxt.text = "Password is not the same"
            return
        }

        errorTxt.text = ""

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    val user = auth.currentUser
                    val intent = Intent(this@SignUpActivity, ProfileActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
//                    return@addOnCompleteListener
                }
            }

        val genderSelected = findViewById<RadioButton>(selectedRadioButtonId)
        val gender = genderSelected.text.toString()

        val user = HashMap<String, String>()
        user.put("name", name)
        user.put("username", username)
        user.put("age", age)
        user.put("gender", gender)
        user.put("email", email)

        db.collection("users")
            .add(user)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}