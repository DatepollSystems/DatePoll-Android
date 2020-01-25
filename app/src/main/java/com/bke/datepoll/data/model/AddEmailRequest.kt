package com.bke.datepoll.data.model

import com.squareup.moshi.Json

data class AddEmailRequest(
    @field:Json(name = "email_addresses") val emails: List<String>
)