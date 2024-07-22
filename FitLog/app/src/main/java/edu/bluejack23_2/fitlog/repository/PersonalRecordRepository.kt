package edu.bluejack23_2.fitlog.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import edu.bluejack23_2.fitlog.models.BodyPart
import edu.bluejack23_2.fitlog.models.MoveSet
import edu.bluejack23_2.fitlog.models.PersonalRecord
import edu.bluejack23_2.fitlog.models.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PersonalRecordRepository {
    private var db: FirebaseFirestore
    private var auth: FirebaseAuth

    constructor() {
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
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

    fun addPersonalRecord(moveSet: MoveSet, _weight: String, _reps: String, _sets: String, callback: (Response) -> Unit) {
        val moveSetID = moveSet.moveSetID
        val weight = _weight.toInt()
        val reps = _reps.toInt()
        val sets = _sets.toInt()
        val currentUser = auth.currentUser
        val uid = currentUser!!.uid

        val personalRecord = PersonalRecord(uid, moveSetID, weight, reps, sets)

        db.collection("personalRecords")
            .whereEqualTo("uid", uid)
            .whereEqualTo("moveSetID", moveSetID)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    db.collection("personalRecords")
                        .add(personalRecord)
                        .addOnSuccessListener { doc ->
                            callback(Response(true, "Successfully added Personal Record"))
                        }
                        .addOnFailureListener { e ->
                            callback(Response(false, "Error adding document $e"))
                        }
                } else {
                    val docRef = documents.documents[0].reference
                    docRef.set(personalRecord)
                        .addOnSuccessListener {
                            callback(Response(true, "Successfully updated Personal Record"))
                        }
                        .addOnFailureListener { e ->
                            callback(Response(false, "Error updating document: $e"))
                        }
                }
            }
            .addOnFailureListener { e ->
                callback(Response(false, "Error checking existing records: $e"))
            }
    }

}