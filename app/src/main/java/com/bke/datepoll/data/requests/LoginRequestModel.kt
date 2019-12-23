package com.bke.datepoll.data.requests

data class LoginRequestModel(
    val username: String,
    val password: String,
    val session_information: String = "Android Smartphone",
    val stay_logged_in: Boolean = true
)