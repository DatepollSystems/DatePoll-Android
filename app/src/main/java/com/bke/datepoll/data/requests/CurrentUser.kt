package com.bke.datepoll.data.requests

import com.bke.datepoll.data.model.UserModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentUserResponseModel(
    val msg: String,
    val user : UserModel
)

