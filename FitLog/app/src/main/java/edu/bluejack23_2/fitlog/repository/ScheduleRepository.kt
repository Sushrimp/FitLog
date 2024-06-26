package edu.bluejack23_2.fitlog.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import edu.bluejack23_2.fitlog.models.BodyPart
import edu.bluejack23_2.fitlog.models.Schedule
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class ScheduleRepository {
    private var db: FirebaseFirestore

    constructor() {
        db = FirebaseFirestore.getInstance()
    }

    fun getScheduleByUid(uid: String, callback: (List<Schedule>?, String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val schedules = mutableListOf<Schedule>()
                val scheduleQuerySnapshot = db.collection("schedules")
                    .whereEqualTo("uid", uid)
                    .get()
                    .await()

                for (document in scheduleQuerySnapshot) {
                    val bodyParts = mutableListOf<BodyPart>()
                    val bodyPartsSnapshot = db.collection("schedules").document(document.id)
                        .collection("bodyParts")
                        .get()
                        .await()

                    for (bodyPartDoc in bodyPartsSnapshot) {
                        val bodyPartID = bodyPartDoc.getString("bodyPart")!!
                        val bodyPartDocSnapshot = db.collection("bodyParts")
                            .document(bodyPartID)
                            .get()
                            .await()
                        val bodyPart = BodyPart(
                            bodyPartID = bodyPartDocSnapshot.id,
                            bodyPart = bodyPartDocSnapshot.getString("bodyPart")
                        )
                        bodyParts.add(bodyPart)
                    }

                    val schedule = Schedule(
                        scheduleID = document.id,
                        scheduleDate = document.getDate("scheduleDate"),
                        bodyParts = bodyParts.toList()
                    )
                    schedules.add(schedule)
                }
                callback(schedules, null)
            } catch (e: Exception) {
                callback(null, e.message)
            }
        }
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
