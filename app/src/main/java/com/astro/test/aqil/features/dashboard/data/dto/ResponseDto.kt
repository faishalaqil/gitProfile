package com.astro.test.aqil.features.dashboard.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseDto(
    @field:Json(name = "status")
    val status: String?,
    @field:Json(name = "code")
    val code: String?,
    @field:Json(name = "message")
    val message: String?
)