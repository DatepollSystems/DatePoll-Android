package com.datepollsystems.datepoll.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Instances(
    val instances: List<Instance>
)

@JsonClass(generateAdapter = true)
data class Instance (
    @field:Json(name = "website_url") val websiteURL: String,
    @field:Json(name = "url") val appURL: String,
    @field:Json(name = "name") val name: String
)