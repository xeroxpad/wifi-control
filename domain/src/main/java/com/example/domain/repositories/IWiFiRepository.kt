package com.example.domain.repositories

import com.example.domain.entities.Router
import com.example.domain.entities.WiFiNetwork
import kotlinx.coroutines.flow.Flow

interface IWiFiRepository {
    suspend fun getAvailableNetworks(): List<WiFiNetwork>
    suspend fun scanLocalNetwork(): List<Router>
}