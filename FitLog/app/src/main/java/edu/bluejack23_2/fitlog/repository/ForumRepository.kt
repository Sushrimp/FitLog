package edu.bluejack23_2.fitlog.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack23_2.fitlog.models.BodyPart
import edu.bluejack23_2.fitlog.models.Forum
import edu.bluejack23_2.fitlog.models.Response
import edu.bluejack23_2.fitlog.models.User

class ForumRepository {
    private var db : FirebaseFirestore
    private var userRepo : UserRepository

    constructor(){
        db = FirebaseFirestore.getInstance()
        userRepo = UserRepository()
    }

    fun getAllForum(callback: (ArrayList<Forum>?) -> Unit) {
        val forums = ArrayList<Forum>()

        db.collection("forums").get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot != null) {
                    for (doc in querySnapshot.documents) {
                        val forum = Forum(
                            doc.id,
                            doc.getString("posterUid"),
                            doc.getString("name"),
                            doc.getString("username"),
                            doc.getString("content")
                        )
                        forums.add(forum)
                    }
                    callback(forums)
                } else {
                    println("ForumRepository, QuerySnapshot is null")
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                println("ForumRepository Error getting documents: " + exception)
                callback(null)
            }
    }

    fun getForum(forumId: String, callback: (Forum?) -> Unit) {
        db.collection("forums").document(forumId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val forum = Forum(
                        doc.id,
                        doc.getString("posterUid"),
                        doc.getString("name"),
                        doc.getString("username"),
                        doc.getString("content")
                    )
                    callback(forum)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                callback(null)
            }
    }

    fun addForum(content: String, callback: (Response) -> Unit) {
        var currentUser: User? = null

        val userRepo = UserRepository()
        userRepo.getUserDetails { response ->
            currentUser = if (response.status) response.user else {
                println("Failed to get user details: ${response.msg}")
                null
            }
        }

        val newForum = hashMapOf(
            "posterUid" to (currentUser?.uid ?: String),
            "name" to (currentUser?.name ?: String),
            "username" to (currentUser?.username ?: String),
            "content" to content
        )

        db.collection("forums").add(newForum)
            .addOnSuccessListener { documentReference ->
                callback(Response(true, "Forum added successfully with ID: ${documentReference.id}"))
            }
            .addOnFailureListener { exception ->
                callback(Response(false, "Error adding forum: ${exception.message}"))
            }

    }
}