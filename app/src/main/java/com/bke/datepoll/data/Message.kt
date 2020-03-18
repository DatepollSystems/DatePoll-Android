package com.bke.datepoll.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Message(
    val msg: String
)