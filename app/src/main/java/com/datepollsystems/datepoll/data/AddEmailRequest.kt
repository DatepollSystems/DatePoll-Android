package com.datepollsystems.datepoll.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddEmailRequest(
    @field:Json(name = "email_addresses") val emails: List<String>
)