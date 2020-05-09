package com.datepollsystems.datepoll.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorMsg(
    val msg: String,
    val error_code: String
)