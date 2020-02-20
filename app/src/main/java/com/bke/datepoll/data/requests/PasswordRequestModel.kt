package com.bke.datepoll.data.requests

import com.squareup.moshi.Json

data class PasswordRequestModel(
    val password: String
)

data class ChangePasswordRequestModel(
    @field:Json(name = "old_password") val oldPassword: String,
    @field:Json(name = "new_password") val newPassword: String
)