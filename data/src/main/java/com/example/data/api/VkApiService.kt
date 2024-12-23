package com.example.data.api

import com.example.data.entities.VKProfileInfoResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface VkApiService {
    @GET("account.getProfileInfo?v=5.199")
    suspend fun loadProfileInfo(
        @Query("access_token") token: String
    ): VKProfileInfoResponseDto
}