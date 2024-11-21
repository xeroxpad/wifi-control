package com.example.data.repositories

import com.example.data.api.SpeedApi
import com.example.domain.entities.SpeedTestResult
import com.example.domain.repositories.ISpeedTestRepository

class ISpeedTestRepositoryImpl(private val api: SpeedApi): ISpeedTestRepository {
    override suspend fun getSpeedTestData(): SpeedTestResult {
        val response = api.getSpeedTestData()
        return SpeedTestResult(
            downloadSpeed = response.download,
            uploadSpeed = response.upload
        )
    }
}