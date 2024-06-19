package edu.bluejack23_2.fitlog.models

// Status
// False means  failed
// True means   success
data class Response(val status: Boolean, val msg: String)

data class UserResponse(val status: Boolean, val user: User, val msg: String)