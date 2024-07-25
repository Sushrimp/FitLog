package edu.bluejack23_2.fitlog.handler

import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack23_2.fitlog.models.Forum
import edu.bluejack23_2.fitlog.models.Response
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

    fun getForum(forumId: String, callback: (Forum?) -> Unit) {
        repo.getForum(forumId){ forum ->
            callback(forum)
        }
    }

    fun stopListening() {
        repo.stopListening()
    }

    fun addReply(forumId: String, newReply: String, callback: (Response) -> Unit) {
        if(!newReply.isNotBlank()){
            callback(Response(false, "Reply can't be empty"))
            return
        }

        repo.addReply(forumId, newReply) {response ->
            callback(response)
        }
    }
}