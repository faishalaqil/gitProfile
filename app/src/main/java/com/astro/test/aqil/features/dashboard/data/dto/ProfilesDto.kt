package com.astro.test.aqil.features.dashboard.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfilesDto(
    @field:Json(name = "login")
    val login: String?,
    @field:Json(name = "id")
    val id: String?,
    @field:Json(name = "node_id")
    val node_id: String?,
    @field:Json(name = "avatar_url")
    val avatar_url: String?
)