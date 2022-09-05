package com.astro.test.aqil.features.dashboard.data.api

import com.astro.test.aqil.features.dashboard.utils.Constant
import com.astro.test.aqil.features.dashboard.data.dto.ProfilesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("users")
    suspend fun getProfiles(@Header("Authorization") apiKey:String = Constant.API_KEY): Response<List<ProfilesDto>>
}