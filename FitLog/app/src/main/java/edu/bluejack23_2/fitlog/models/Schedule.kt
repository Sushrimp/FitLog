package edu.bluejack23_2.fitlog.models

import java.util.Date

data class Schedule(
    val scheduleID: String,
    val scheduleDate: Date?,
    val bodyParts: List<BodyPart>?
)