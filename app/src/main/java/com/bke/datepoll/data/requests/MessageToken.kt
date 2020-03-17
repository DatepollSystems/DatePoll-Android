package com.bke.datepoll.data.requests

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageToken(
    val msg: String,
    val token: String
)