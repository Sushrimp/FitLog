package edu.bluejack23_2.fitlog.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack23_2.fitlog.models.Response
import edu.bluejack23_2.fitlog.models.StorageResponse
import edu.bluejack23_2.fitlog.models.User
import edu.bluejack23_2.fitlog.models.UserResponse

class UserRepository {
    private var db : FirebaseFirestore
    private var storage : FirebaseStorage
    private var auth : FirebaseAuth

    constructor(){
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    fun getProfilePicture(callback: (StorageResponse) -> Unit) {
        val currentUser = auth.currentUser
        val storageRef = storage.reference

        if (currentUser == null) {
            val fileRef = storageRef.child("profilePictures/emptyProfile.jpg")
            val response = StorageResponse(true, fileRef)
            callback(response)
        } else {
            val uid = currentUser.uid
            val userFileRef = storageRef.child("profilePictures/$uid.png")

            userFileRef.metadata
                .addOnSuccessListener { metadata ->
                    val response = StorageResponse(true, userFileRef)
                    callback(response)
                }
                .addOnFailureListener { exception ->
                    val emptyFileRef = storageRef.child("profilePictures/emptyProfile.jpg")
                    val response = StorageResponse(false, emptyFileRef)
                    callback(response)
                }
        }
    }

    fun getUserDetails(callback: (UserResponse) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid

            db.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    if (doc != null && doc.exists()) {
                        val response = UserResponse(
                            true,
                            User(
                                uid = uid,
                                age = doc.getString("age"),
                                email = doc.getString("email"),
                                username = doc.getString("username"),
                                name = doc.getString("name"),
                                gender = doc.getString("gender")
                            ),
                            "User detail retrieved successfully"
                        )
                        callback(response)
                    }
                    else {
                        val response = UserResponse(
                            false,
                            User(
                                uid = "",
                                age = "",
                                email = "",
                                username = "",
                                name = "",
                                gender = ""
                            ),
                            "User detail retrieve failed"
                        )
                        callback(response)
                    }
                }
        }
    }
}