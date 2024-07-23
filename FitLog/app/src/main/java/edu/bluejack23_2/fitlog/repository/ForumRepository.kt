package edu.bluejack23_2.fitlog.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

        db.collection("forums")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
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
                        doc.getString("content"),
                        doc.get("replies") as? List<String>
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
        val userRepo = UserRepository()

        userRepo.getUserDetails { response ->
            val currentUser = response.user
            if (response.status && currentUser != null) {
                val newForum = hashMapOf(
                    "posterUid" to currentUser.uid,
                    "name" to currentUser.name,
                    "username" to currentUser.username,
                    "content" to content,
                    "timestamp" to FieldValue.serverTimestamp()
                )

                db.collection("forums").add(newForum)
                    .addOnSuccessListener { documentReference ->
                        callback(Response(true, "Forum added successfully with ID: ${documentReference.id}"))
                    }
                    .addOnFailureListener { exception ->
                        callback(Response(false, "Error adding forum: ${exception.message}"))
                    }
            } else {
                callback(Response(false, "Failed to get user details: ${response.msg}"))
            }
        }
    }

    fun addReply(forumId: String, newReply: String, callback: (Response) -> Unit) {
        val forumDocRef = db.collection("forums").document(forumId)

        if(forumDocRef != null){
            forumDocRef.get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        val replies = doc.get("replies") as? MutableList<String> ?: mutableListOf()
                        replies.add(newReply)

                        forumDocRef.update("replies", replies)
                            .addOnSuccessListener {
                                callback(Response(true, "Reply added successfully"))
                            }
                            .addOnFailureListener { exception ->
                                callback(Response(false, "Error adding reply: ${exception.message}"))
                            }
                    } else {
                        callback(Response(false, "Forum not found"))
                    }
                }
                .addOnFailureListener { exception ->
                    callback(Response(false, "Error retrieving forum: ${exception.message}"))
                }
        } else {
            callback(Response(false, "Error retrieving forum"))
        }
    }
}