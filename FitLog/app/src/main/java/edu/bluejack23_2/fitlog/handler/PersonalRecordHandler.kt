package edu.bluejack23_2.fitlog.handler

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Spinner
import android.widget.Toast
import edu.bluejack23_2.fitlog.models.BodyPartSpinnerAdapter
import edu.bluejack23_2.fitlog.models.MoveSetSpinnerAdapter
import edu.bluejack23_2.fitlog.repository.PersonalRecordRepository

class PersonalRecordHandler {
    private var repo: PersonalRecordRepository
    constructor(){
        repo = PersonalRecordRepository()
    }

    fun setAllBodyParts(context: Context?, bodyPartSpinner: Spinner) {
        repo.getAllBodyParts { bodyParts ->
            if(bodyParts != null && context != null) {
                val adapter = BodyPartSpinnerAdapter(context, bodyParts)
                bodyPartSpinner.adapter = adapter
            } else {
                Toast.makeText(context, "Error fetching Body Parts", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setMoveSets(context: Context?, bodyPart: String, moveSetSpinner: Spinner) {
        println(bodyPart)
        repo.getMoveSetsByBodyPart(bodyPart) { moveSets, error ->
            if (moveSets != null && context != null && error == null) {
                (context as? Activity)?.runOnUiThread {
                    try {
                        val adapter = MoveSetSpinnerAdapter(context, moveSets)
                        moveSetSpinner.adapter = adapter
                    } catch (e: Exception) {
                        Log.e("PersonalRecordHandler", "Error setting MoveSetSpinnerAdapter", e)
                    }
                }
            } else {
                (context as? Activity)?.runOnUiThread {
                    Toast.makeText(context, "Error fetching Move Sets: $error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}