package com.bke.datepoll.data.requests

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LogoutResponseModel(
    val username: String
)