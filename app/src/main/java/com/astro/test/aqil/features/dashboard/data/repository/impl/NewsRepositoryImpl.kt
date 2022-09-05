package com.astro.test.aqil.features.dashboard.data.repository.impl

import com.astro.test.aqil.features.dashboard.data.api.ApiService
import com.astro.test.aqil.features.dashboard.data.repository.NewsRepository
import com.astro.test.aqil.features.dashboard.data.dto.ProfilesDto
import org.koin.core.KoinComponent
import retrofit2.Response

class NewsRepositoryImpl(
    private val apiService: ApiService //constructor injection
) : NewsRepository, KoinComponent {

    override suspend fun getProfiles(): Response<List<ProfilesDto>> {
        return apiService.getProfiles()
    }
}