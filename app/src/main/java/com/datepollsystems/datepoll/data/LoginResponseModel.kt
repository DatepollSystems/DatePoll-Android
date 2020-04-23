package com.datepollsystems.datepoll.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponseModel (
    val token: String,
    val session_token: String
)