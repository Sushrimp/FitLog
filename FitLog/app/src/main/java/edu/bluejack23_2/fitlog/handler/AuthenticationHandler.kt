package edu.bluejack23_2.fitlog.handler

import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.auth.EmailAuthProvider
import edu.bluejack23_2.fitlog.SignUpActivity
import edu.bluejack23_2.fitlog.models.Response
import edu.bluejack23_2.fitlog.repository.AuthenticationRepository
import java.util.*

class AuthenticationHandler {
    private var repo : AuthenticationRepository

    constructor(){
        repo = AuthenticationRepository()
    }

    fun signIn(email: String, password: String, callback: (Response) -> Unit) {
        if (email.equals("") || password.equals("")) {
            val response = Response(false, "All fields must be filled")
            callback(response)
            return
        }

        repo.signIn(email, password) { response ->
            callback(response)
        }
    }

    fun signUp(name : String, username: String, age: String, email: String, password: String, confirmPassword : String,
               genderGroup: RadioGroup, genderSelected : RadioButton, callback: (Response) -> Unit){
        val selectedRadioButtonId = genderGroup.checkedRadioButtonId

        if(password.length < 8 || password.length > 20){
            val response = Response(false, "Password length must be between 8 and 20 characters")
            callback(response)
            return
        }
        else if (password != confirmPassword) {
            val response = Response(false, "Password must be the same")
            callback(response)
            return
        }

        val gender = genderSelected.text.toString()

        val user = HashMap<String, String>()
        user.put("name", name)
        user.put("username", username)
        user.put("age", age)
        user.put("gender", gender)
        user.put("email", email)

        repo.signUp(user, email, password){response ->
            callback(response)
        }
    }

    fun signOut(callback: (Response) -> Unit){
        repo.signOut(){response ->
            callback(response)
        }
    }

    fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String, callback: (Response) -> Unit){
        if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()){
            callback(Response(false, "All field must be filled"))
            return
        }

        if(newPassword.length < 8 || newPassword.length > 20){
            val response = Response(false, "New Password length must be between 8 and 20 characters")
            callback(response)
            return
        }

        if(!newPassword.equals(confirmPassword)){
            callback(Response(false, "New Password and Confirm Password must be the same"))
            return
        }

        repo.changePassword(oldPassword, newPassword){ response ->
            callback(response)
        }
    }

}