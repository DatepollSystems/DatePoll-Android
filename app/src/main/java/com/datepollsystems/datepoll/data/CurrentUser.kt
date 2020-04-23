package com.datepollsystems.datepoll.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentUserResponseModel(
    val msg: String,
    val user : UserModel
)

