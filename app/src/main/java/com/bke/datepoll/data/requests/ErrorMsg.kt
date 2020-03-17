package com.bke.datepoll.data.requests

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorMsg(
    val msg: String,
    val error_code: String
)