package com.example.domain.usecases

import com.example.domain.entities.Router
import com.example.domain.repositories.IWiFiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ScanLocalNetworkUseCase(private val repository: IWiFiRepository) {
    fun execute(): Flow<List<Router>> = flow {
        val router = repository.scanLocalNetwork()
        emit(router)
    }
}