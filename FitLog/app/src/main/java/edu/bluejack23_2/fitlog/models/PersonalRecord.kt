package edu.bluejack23_2.fitlog.models

data class PersonalRecord (
    val uid: String,
    val moveSetID: String,
    val weight: Int,
    val reps: Int,
    val sets: Int
)

data class PersonalRecordDetail (
    val uid: String,
    val moveSet: MoveSet,
    val weight: Int,
    val reps: Int,
    val sets: Int
)