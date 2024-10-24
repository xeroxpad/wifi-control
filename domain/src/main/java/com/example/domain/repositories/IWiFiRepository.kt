package com.example.domain.repositories

import com.example.domain.entities.WiFiNetwork

interface IWiFiRepository {
    suspend fun getAvailableNetworks(): List<WiFiNetwork>
}