package edu.bluejack23_2.fitlog.handler

import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import edu.bluejack23_2.fitlog.models.Response
import edu.bluejack23_2.fitlog.models.UserResponse
import edu.bluejack23_2.fitlog.repository.UserRepository

class UserHandler {
    private var repo : UserRepository

    constructor() {
        repo = UserRepository()
    }

    fun setProfilePicture(profilePicture: ImageView) {
        repo.getProfilePicture { response ->
            val fileRef = response.msg
            fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                Glide.with(profilePicture.context)
                    .load(downloadUrl)
                    .into(profilePicture)
            }.addOnFailureListener { exception ->
                Toast.makeText(profilePicture.context, "Failed to get download URL: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setProfilePictureById(userId: String, profilePicture: ImageView) {
        repo.getProfilePictureById(userId) { response ->
            val fileRef = response.msg
            fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                Glide.with(profilePicture.context)
                    .load(downloadUrl)
                    .into(profilePicture)
            }.addOnFailureListener { exception ->
                Toast.makeText(profilePicture.context, "Failed to get download URL: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getUserDetails(callback: (UserResponse) -> Unit){
        repo.getUserDetails { response ->
            callback(response)
        }
    }

    fun updateUser(name: String, username: String, age: String, gender: String, callback: (Response) -> Unit){
        if (!name.isNotEmpty() || !username.isNotEmpty()) {
            val response = Response(false, "All fields must be filled")
            callback(response)
            return
        }

        repo.updateUser(name, username, age, gender) { response ->
            callback(response)
        }
    }
}