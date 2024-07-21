package edu.bluejack23_2.fitlog.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import edu.bluejack23_2.fitlog.models.BodyPart
import edu.bluejack23_2.fitlog.models.MoveSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PersonalRecordRepository {
    private var db: FirebaseFirestore

    constructor() {
        db = FirebaseFirestore.getInstance()
    }

    fun getAllBodyParts(callback: (List<BodyPart>?) -> Unit) {
        val bodyParts = mutableListOf <BodyPart>()
        val dummy = BodyPart(
            "0", "Select Body Part"
        )
        bodyParts.add(dummy)
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

    fun getMoveSetsByBodyPart(bodyPart: String, callback: (List<MoveSet>?, String?) -> Unit) {
        val moveSets = mutableListOf<MoveSet>()
        val dummy = MoveSet(
            "0", "Select Move Set"
        )
        moveSets.add(dummy)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val moveSetSnapshot = db.collection("moveSets")
                    .whereEqualTo("bodyPart", bodyPart)
                    .get()
                    .await()

                for (doc in moveSetSnapshot) {
                    val moveSet = MoveSet(
                        moveSetID = doc.id,
                        moveSet = doc.getString("moveSet")!!
                    )
                    moveSets.add(moveSet)
                }
                callback(moveSets, null)
            } catch (e: Exception) {
                callback(null, e.message)
            }
        }
    }
}