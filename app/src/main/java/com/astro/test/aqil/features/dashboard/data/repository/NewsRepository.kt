package com.astro.test.aqil.features.dashboard.data.repository

import com.astro.test.aqil.features.dashboard.data.dto.ProfilesDto
import retrofit2.Response

interface NewsRepository {
    suspend fun getProfiles(): Response<List<ProfilesDto>>
}