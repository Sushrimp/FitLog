package edu.bluejack23_2.fitlog.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack23_2.fitlog.models.BodyPart
import edu.bluejack23_2.fitlog.models.Forum

class ForumRepository {
    private var db : FirebaseFirestore

    constructor(){
        db = FirebaseFirestore.getInstance()
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

    }
}