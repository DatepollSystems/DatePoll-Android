package com.datepollsystems.datepoll.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShownInBirthdayListResponse(
    val msg: String,
    @field:Json(name = "setting_key") val settingsKey: String,
    @field:Json(name = "setting_value") val settingsValue: Boolean
)

@JsonClass(generateAdapter = true)
data class PostShownInBirthdayListRequest (
    @field:Json(name = "setting_value") val value: Boolean
)