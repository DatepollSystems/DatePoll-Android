package com.bke.datepoll.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Session(
    val msg: String,
    val sessions: List<SessionModel>
)

@JsonClass(generateAdapter = true)
data class SessionModel(
    val id: Int,
    val information: String,
    @field:Json(name = "last_used") val lastUsed: String,
    @field:Json(name = "delete_session") val deleteSession: DeleteSession
)

@JsonClass(generateAdapter = true)
data class DeleteSession(
    val href: String,
    val method: String
)