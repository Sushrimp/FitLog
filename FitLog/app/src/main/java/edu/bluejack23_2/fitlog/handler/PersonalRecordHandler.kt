package edu.bluejack23_2.fitlog.handler

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack23_2.fitlog.models.BodyPartSpinnerAdapter
import edu.bluejack23_2.fitlog.models.MoveSet
import edu.bluejack23_2.fitlog.models.MoveSetSpinnerAdapter
import edu.bluejack23_2.fitlog.models.PersonalRecord
import edu.bluejack23_2.fitlog.models.PersonalRecordDetail
import edu.bluejack23_2.fitlog.models.Response
import edu.bluejack23_2.fitlog.repository.AuthenticationRepository
import edu.bluejack23_2.fitlog.repository.ForumRepository
import edu.bluejack23_2.fitlog.repository.PersonalRecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersonalRecordHandler {
    private var repo: PersonalRecordRepository
    private var authRepo: AuthenticationRepository
    private var forumRepo: ForumRepository
    constructor(){
        repo = PersonalRecordRepository()
        authRepo = AuthenticationRepository()
        forumRepo = ForumRepository()
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

    fun addPersonalRecord(moveSet: MoveSet, weight: String, reps: String, sets: String, forumCheck: Boolean, callback: (Response) -> Unit) {
        authRepo.getAuthorizedUser { uid ->
            if (uid == null) {
                callback(Response(false, "You are not logged in"))
            } else {
                if(moveSet.moveSetID == "0") {
                    callback(Response(false, "Please select a move set"))
                } else if(weight.isEmpty() || weight.toInt() <= 0) {
                    callback(Response(false, "Please fill the weight field with number greater than 0"))
                } else if(reps.isEmpty() || reps.toInt() <= 0) {
                    callback(Response(false, "Please fill the reps field with number greater than 0"))
                } else if(sets.isEmpty() || sets.toInt() <= 0) {
                    callback(Response(false, "Please fill the sets field with number greater than 0"))
                } else {
                    repo.addPersonalRecord(moveSet, weight, reps, sets) {response ->
                        if(forumCheck && response.status) {
                            val forumString = "I’m happy to say that I finally got a new PR for ${moveSet.moveSet} with ${weight}kg weight, $reps reps, $sets sets."
                            forumRepo.addForum(forumString) {response ->
                                if(response.status) {
                                    callback(Response(true, "Successfully added Personal Record and Forum!"))
                                } else {
                                    callback(response)
                                }
                            }
                        } else if(response.status) {
                            callback(response)
                        } else {
                            callback(response)
                        }
                    }
                }
            }
        }
    }

    fun getUserPersonalRecord(callback: (personalRecords: List<PersonalRecordDetail>?, message: String?) -> Unit) {
        repo.getUserPersonalRecord { personalRecords, message ->
            callback(personalRecords, message)
        }
    }

}