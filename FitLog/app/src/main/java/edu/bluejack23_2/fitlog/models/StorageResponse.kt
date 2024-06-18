package edu.bluejack23_2.fitlog.models

import com.google.firebase.storage.StorageReference

// Status
// False means  failed
// True means   success
data class StorageResponse(val status: Boolean, val msg: StorageReference)