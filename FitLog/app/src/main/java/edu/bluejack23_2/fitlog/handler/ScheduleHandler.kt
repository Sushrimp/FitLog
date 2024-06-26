package edu.bluejack23_2.fitlog.handler

import com.google.firebase.auth.FirebaseAuth
import com.google.rpc.context.AttributeContext.Response
import edu.bluejack23_2.fitlog.models.BodyPart
import edu.bluejack23_2.fitlog.models.Schedule
import edu.bluejack23_2.fitlog.repository.ScheduleRepository
import java.util.Calendar

class ScheduleHandler {
    private var repo: ScheduleRepository
    private var auth: FirebaseAuth

    constructor() {
        repo = ScheduleRepository()
        auth = FirebaseAuth.getInstance()
    }

    fun getTodaySchedule(callback: (Schedule?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(null)
        } else {
            val uid = currentUser.uid
            repo.getScheduleByUid(uid) { schedules, error ->
                if (error != null) {
                    println("Error fetching schedules: $error")
                } else if (schedules != null) {
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    var currDate = calendar.time
                    var filteredSchedule = schedules.find {
                        val scheduleCalendar = Calendar.getInstance().apply {
                            time = it.scheduleDate
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }

                        val currCalendar = Calendar.getInstance().apply {
                            time = currDate
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }

                        scheduleCalendar.timeInMillis == currCalendar.timeInMillis
                    }
                    if (filteredSchedule == null) {
                        callback(null)
                    } else {
                        callback(filteredSchedule)
                    }
                } else {
                    callback(null)
                }
            }
        }
    }

    fun getAllBodyParts(callback: (List<BodyPart>?) -> Unit){
        repo.getAllBodyParts { bodyParts ->
            if(bodyParts != null) {
                callback(bodyParts)
            } else {
                callback(null)
            }
        }
    }
}