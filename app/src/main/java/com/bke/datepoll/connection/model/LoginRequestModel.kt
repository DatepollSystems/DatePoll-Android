package com.bke.datepoll.connection.model

data class LoginRequestModel(
    val username: String,
    val password: String,
    val session_information: String = "Android Smartphone",
    val stay_logged_in: Boolean = true
)