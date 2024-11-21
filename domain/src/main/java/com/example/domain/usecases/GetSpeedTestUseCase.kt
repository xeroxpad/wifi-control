package com.example.domain.usecases

import com.example.domain.entities.SpeedTestResult
import com.example.domain.repositories.ISpeedTestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetSpeedTestUseCase(private val speedTestRepository: ISpeedTestRepository) {
    fun execute(): Flow<SpeedTestResult> = flow{
        val speedTest = speedTestRepository.getSpeedTestData()
        emit(speedTest)
    }
}