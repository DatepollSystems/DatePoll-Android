package com.datepollsystems.datepoll.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordRequestModel(
    val password: String
)

@JsonClass(generateAdapter = true)
data class ChangePasswordRequestModel(
    @field:Json(name = "old_password") val oldPassword: String,
    @field:Json(name = "new_password") val newPassword: String
)