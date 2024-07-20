package edu.bluejack23_2.fitlog.repository

import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack23_2.fitlog.models.BodyPart

class PersonalRecordRepository {
    private var db: FirebaseFirestore

    constructor() {
        db = FirebaseFirestore.getInstance()
    }

    fun getAllBodyParts(callback: (List<BodyPart>?) -> Unit) {
        val bodyParts = mutableListOf <BodyPart>()
        db.collection("bodyParts").get().addOnSuccessListener {querySnapshot ->
            for(doc in querySnapshot) {
                val bodyPart = BodyPart(doc.id, doc.getString("bodyPart"))
                bodyParts.add(bodyPart)
            }
            callback(bodyParts)
        }.addOnFailureListener {
            callback(null)
        }
    }
}