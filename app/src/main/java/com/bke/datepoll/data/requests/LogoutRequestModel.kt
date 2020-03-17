package com.bke.datepoll.data.requests

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LogoutRequestModel(
    val session_token: String
)