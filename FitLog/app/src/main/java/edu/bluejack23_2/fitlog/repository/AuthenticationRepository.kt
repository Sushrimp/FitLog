package edu.bluejack23_2.fitlog.repository

import android.content.Intent
import android.widget.RadioGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack23_2.fitlog.HomeActivity
import edu.bluejack23_2.fitlog.models.Response
import java.util.*

class AuthenticationRepository {
    private var db : FirebaseFirestore
    private var auth : FirebaseAuth

    constructor(){
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    fun signIn(email: String, password: String, callback: (Response) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                val response = if (task.isSuccessful) {
                    Response(true, "Signed in successfully")
                } else {
                    Response(false, "Email or Password is incorrect")
                }
                callback(response)
            }
    }

    fun signUp(user: HashMap<String, String>, email: String, password: String, callback: (Response) -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                val response = if (task.isSuccessful) {
                    db.collection("users")
                        .add(user)

                    Response(true, "Signed up successfully")

                } else {
                    Response(false, "Email is in used")
                }
                callback(response)
            }
    }

    fun signOut(callback: (Response) -> Unit){
        auth.signOut()
        callback(Response(true, "Signed Out"))
    }


}