package com.example.domain.usecases

import com.example.domain.entities.WiFiNetwork
import com.example.domain.repositories.IWiFiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWiFiNetworkUseCase(private val wifiRepository: IWiFiRepository) {
    fun execute(): Flow<List<WiFiNetwork>> = flow {
        val wiFiNetwork = wifiRepository.getAvailableNetworks()
        emit(wiFiNetwork)
    }
}