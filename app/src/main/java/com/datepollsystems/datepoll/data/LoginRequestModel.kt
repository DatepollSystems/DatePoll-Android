package com.datepollsystems.datepoll.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequestModel(
    val username: String,
    val password: String,
    val session_information: String = "${android.os.Build.MANUFACTURER} ${android.os.Build.DEVICE}",
    val stay_logged_in: Boolean = true
)