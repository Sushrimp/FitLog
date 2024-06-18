package edu.bluejack23_2.fitlog.handler

import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import edu.bluejack23_2.fitlog.repository.HomeRepository

class HomeHandler {
    private var repo : HomeRepository

    constructor() {
        repo = HomeRepository()
    }

    fun setProfilePicture(profilePicture : ImageView) {
        repo.getProfilePicture { response ->
            var fileRef = response.msg
            fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                Glide.with(profilePicture.context)
                    .load(downloadUrl)
                    .into(profilePicture)
            }.addOnFailureListener { exception ->
                Toast.makeText(profilePicture.context, "Failed to get download URL: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}