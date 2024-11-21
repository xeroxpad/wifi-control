package com.example.domain.repositories

import com.example.domain.entities.SpeedTestResult


interface ISpeedTestRepository {
    suspend fun getSpeedTestData(): SpeedTestResult
}