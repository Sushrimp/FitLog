package edu.bluejack23_2.fitlog.handler

import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack23_2.fitlog.models.Forum
import edu.bluejack23_2.fitlog.repository.ForumRepository

class ForumHandler {
    private var repo : ForumRepository

    constructor(){
        repo = ForumRepository()
    }

    fun getAllForum(callback: (ArrayList<Forum>?) -> Unit) {
        repo.getAllForum { forums ->
            callback(forums)
        }
    }
}