package com.example.domain.repositories

import com.example.domain.entities.WiFiNetwork
import kotlinx.coroutines.flow.Flow

interface IWiFiRepository {
    fun getAvailableNetworks(): List<WiFiNetwork>
}