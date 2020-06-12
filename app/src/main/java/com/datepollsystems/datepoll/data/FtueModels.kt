package com.datepollsystems.datepoll.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FirstPasswdChangeRequest(
    val username: String,
    @field:Json(name = "old_password") val oldPassword: String,
    @field:Json(name = "new_password") val newPassword: String,
    @field:Json(name = "session_information") val sessionInfo: String = "${android.os.Build.MANUFACTURER} ${android.os.Build.DEVICE}",
    @field:Json(name = "stay_logged_in") val stayLoggedIn: Boolean = true
)