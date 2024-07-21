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

    private fun getAllForum(callback: (List<Forum>?) -> Unit) {
        val forums = mutableListOf <Forum>()
        db.collection("forum").get().addOnSuccessListener { querySnapshot ->
            for(doc in querySnapshot){
                val forum = Forum(
                    doc.id,
                    doc.getString("posterUid"),
                    doc.getString("name"),
                    doc.getString("username"),
                    doc.getString("content"),
                )
                forums.add(forum)
            }
            callback(forums)
        }.addOnFailureListener {
            callback(null)
        }
    }
}