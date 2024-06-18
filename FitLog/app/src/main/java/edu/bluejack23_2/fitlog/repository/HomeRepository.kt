package edu.bluejack23_2.fitlog.repository

import android.content.Intent
import android.widget.RadioGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack23_2.fitlog.HomeActivity
import edu.bluejack23_2.fitlog.models.Response
import edu.bluejack23_2.fitlog.models.StorageResponse
import java.util.*

class HomeRepository {
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
            val userFileRef = storageRef.child("profilePictures/$uid.jpg")

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
}