package com.bke.datepoll.data.requests

import com.squareup.moshi.Json

data class RefreshTokenWithSessionRequest(
    @field:Json(name = "session_token") val sessionToken : String,
    @field:Json(name = "session_information") val sessionInformation: String = "Android smartphone"
)

data class RefreshTokenWithSessionResponse(
    val msg: String,
    val token: String
)