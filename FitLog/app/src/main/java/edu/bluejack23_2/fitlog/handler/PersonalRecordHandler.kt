package edu.bluejack23_2.fitlog.handler

import android.content.Context
import android.widget.Spinner
import edu.bluejack23_2.fitlog.models.BodyPartSpinnerAdapter
import edu.bluejack23_2.fitlog.repository.PersonalRecordRepository

class PersonalRecordHandler {
    private var repo: PersonalRecordRepository
    constructor(){
        repo = PersonalRecordRepository()
    }

    fun setAllBodyParts(context: Context?, bodyPartSpinner: Spinner) {
        repo.getAllBodyParts { bodyParts ->
            if(bodyParts != null && context != null) {
                println("Test")
                val adapter = BodyPartSpinnerAdapter(context, bodyParts)
                bodyPartSpinner.adapter = adapter
            }
        }
    }
}