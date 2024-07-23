package edu.bluejack23_2.fitlog.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack23_2.fitlog.models.BodyPart
import edu.bluejack23_2.fitlog.models.MoveSet
import edu.bluejack23_2.fitlog.models.PersonalRecord
import edu.bluejack23_2.fitlog.models.PersonalRecordDetail
import edu.bluejack23_2.fitlog.models.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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

//    fun getUserPersonalRecord(callback: (personalRecords: List<PersonalRecordDetail>?, message: String?) -> Unit) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val currentUser = auth.currentUser
//            val uid = currentUser?.uid
//
//            if (uid != null) {
//                try {
//                    val personalRecords = mutableListOf<PersonalRecordDetail>()
//                    val personalRecordSnapshot = db.collection("personalRecords")
//                        .whereEqualTo("uid", uid)
//                        .get()
//                        .await()
//
//                    val deferreds = personalRecordSnapshot.documents.map { prDoc ->
//                        async {
//                            try {
//                                val moveSetDoc = db.collection("moveSets")
//                                    .document(prDoc.getString("moveSetID")!!)
//                                    .get()
//                                    .await()
//
//                                val moveSet = MoveSet(moveSetDoc.id, moveSetDoc.getString("moveSet"))
//                                val weight = prDoc.getLong("weight")!!.toInt()
//                                val reps = prDoc.getLong("reps")!!.toInt()
//                                val sets = prDoc.getLong("sets")!!.toInt()
//                                val personalRecord = PersonalRecordDetail(uid, moveSet, weight, reps, sets)
//                                personalRecords.add(personalRecord)
//                            } catch (e: Exception) {
//                                callback(null, "Error in fetching Move Set: $e")
//                            }
//                        }
//                    }
//
//                    deferreds.awaitAll()
//
//                    callback(personalRecords, null)
//                } catch (e: Exception) {
//                    callback(null, e.message)
//                }
//            } else {
//                callback(null, "User is not logged in")
//            }
//        }
//    }

    fun getUserPersonalRecord(callback: (personalRecords: List<PersonalRecordDetail>?, message: String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val currentUser = auth.currentUser
            val uid = currentUser?.uid

            if (uid != null) {
                try {
                    val personalRecords = mutableListOf<PersonalRecordDetail>()
                    val personalRecordSnapshot = db.collection("personalRecords")
                        .whereEqualTo("uid", uid)
                        .get()
                        .await()

                    val deferreds = personalRecordSnapshot.documents.map { prDoc ->
                        async {
                            try {
                                val moveSetDoc = db.collection("moveSets")
                                    .document(prDoc.getString("moveSetID")!!)
                                    .get()
                                    .await()

                                val moveSet = MoveSet(moveSetDoc.id, moveSetDoc.getString("moveSet"))
                                val weight = prDoc.getLong("weight")!!.toInt()
                                val reps = prDoc.getLong("reps")!!.toInt()
                                val sets = prDoc.getLong("sets")!!.toInt()
                                val personalRecord = PersonalRecordDetail(uid, moveSet, weight, reps, sets)
                                personalRecords.add(personalRecord)
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    callback(null, "Error in fetching Move Set: $e")
                                }
                            }
                        }
                    }

                    deferreds.awaitAll()

                    withContext(Dispatchers.Main) {
                        callback(personalRecords, null)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        callback(null, e.message)
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    callback(null, "User is not logged in")
                }
            }
        }
    }


}