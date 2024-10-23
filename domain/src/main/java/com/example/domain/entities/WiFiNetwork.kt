package com.example.domain.entities

data class WiFiNetwork(
    val ssid: String,
    val ip: String? = null,
    val macId: String,
    val signalStrength: String,
    val frequency: Int,
)
