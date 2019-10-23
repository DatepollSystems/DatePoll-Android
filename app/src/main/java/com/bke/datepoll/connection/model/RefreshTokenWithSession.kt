package com.bke.datepoll.connection.model

data class RefreshTokenWithSessionRequest(
    val session_token : String,
    val session_information: String = "Android smartphone"
)

data class RefreshTokenWithSessionResponse(
    val msg: String,
    val token: String
)