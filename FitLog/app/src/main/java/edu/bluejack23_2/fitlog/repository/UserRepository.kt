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
            // User is not logged in, return the default empty profile picture
            val fileRef = storageRef.child("profilePictures/emptyProfile.jpg")
            val response = StorageResponse(true, fileRef)
            callback(response)
        } else {
            val uid = currentUser.uid
            val userDirRef = storageRef.child("profilePictures")

            userDirRef.listAll()
                .addOnSuccessListener { result ->
                    // Find the file with the user's UID
                    val userFileRef = result.items.find { it.name.startsWith(uid) }
                    if (userFileRef != null) {
                        val response = StorageResponse(true, userFileRef)
                        callback(response)
                    } else {
                        // No matching file found, return the default empty profile picture
                        val emptyFileRef = storageRef.child("profilePictures/emptyProfile.jpg")
                        val response = StorageResponse(false, emptyFileRef)
                        callback(response)
                    }
                }
                .addOnFailureListener {
                    // Listing files failed, fallback to the default empty profile picture
                    val emptyFileRef = storageRef.child("profilePictures/emptyProfile.jpg")
                    val response = StorageResponse(false, emptyFileRef)
                    callback(response)
                }
        }
    }

    fun getProfilePictureById(userId: String, callback: (StorageResponse) -> Unit) {
        val storageRef = storage.reference

        if (userId == null) {
            // User is not logged in, return the default empty profile picture
            val fileRef = storageRef.child("profilePictures/emptyProfile.jpg")
            val response = StorageResponse(true, fileRef)
            callback(response)
        } else {
            val uid = userId
            val userDirRef = storageRef.child("profilePictures")

            userDirRef.listAll()
                .addOnSuccessListener { result ->
                    // Find the file with the user's UID
                    val userFileRef = result.items.find { it.name.startsWith(uid) }
                    if (userFileRef != null) {
                        val response = StorageResponse(true, userFileRef)
                        callback(response)
                    } else {
                        // No matching file found, return the default empty profile picture
                        val emptyFileRef = storageRef.child("profilePictures/emptyProfile.jpg")
                        val response = StorageResponse(false, emptyFileRef)
                        callback(response)
                    }
                }
                .addOnFailureListener {
                    // Listing files failed, fallback to the default empty profile picture
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
                    } else {
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
                            "User detail retrieval failed: document does not exist"
                        )
                        callback(response)
                    }
                }
                .addOnFailureListener { exception ->
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
                        "User detail retrieval failed: ${exception.message}"
                    )
                    callback(response)
                }
        } else {
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
                "No current user logged in"
            )
            callback(response)
        }
    }

    fun getUserDetailsById(uid: String, callback: (UserResponse) -> Unit) {
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
                } else {
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
                        "User detail retrieval failed: document does not exist"
                    )
                    callback(response)
                }
            }
            .addOnFailureListener { exception ->
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
                    "User detail retrieval failed: ${exception.message}"
                )
                callback(response)
            }
    }

    fun updateUser(name: String, username: String, age: String, gender: String, callback: (Response) -> Unit) {
        val currentUser = auth.currentUser

        if(currentUser != null){
            val userRef = db.collection("users").document(currentUser.uid)
            val userUpdates = hashMapOf(
                "name" to name,
                "username" to username,
                "age" to age,
                "gender" to gender
            )

            userRef.update(userUpdates as Map<String, Any>).addOnCompleteListener { updateTask ->
                if (updateTask.isSuccessful) {
                    callback(Response(true, "User updated successfully"))
                } else {
                    callback(Response(false, "Failed to update user details in Firestore"))
                }
            }
        }
        else {
            callback(Response(false, "Not Authenticated"))
        }
    }


}