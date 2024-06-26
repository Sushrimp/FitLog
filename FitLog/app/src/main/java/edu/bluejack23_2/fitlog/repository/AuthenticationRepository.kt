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

    fun signUp(user: HashMap<String, String>, email: String, password: String, callback: (Response) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User successfully created, now get the UID
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        val uid = currentUser.uid
                        // Add user data to the Firestore database with the UID as the document ID
                        db.collection("users").document(uid)
                            .set(user)
                            .addOnSuccessListener {
                                val response = Response(true, "Signed up successfully")
                                callback(response)
                            }
                            .addOnFailureListener { e ->
                                // Handle the error if setting the document fails
                                val response = Response(false, "Failed to create user document: ${e.message}")
                                callback(response)
                            }
                    } else {
                        val response = Response(false, "Failed to retrieve user UID")
                        callback(response)
                    }
                } else {
                    val response = Response(false, "Email is in use")
                    callback(response)
                }
            }
    }


    fun signOut(callback: (Response) -> Unit){
        auth.signOut()
        callback(Response(true, "Signed Out"))
    }


}