package com.example.data.api

import com.example.data.entities.SpeedTestResponse
import retrofit2.http.GET

interface SpeedApi {
    @GET("speedtest")
    suspend fun getSpeedTestData(): SpeedTestResponse
}