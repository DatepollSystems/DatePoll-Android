package com.bke.datepoll.connection.model

data class LoginResponseModel (
    val token: String,
    val session_token: String
)