package com.bke.datepoll.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponseModel (
    val token: String,
    val session_token: String
)