package com.example.domain.entities

data class WiFiNetwork(
    val ssid: String,
    val macId: String,
    val signalStrength: String,
    val frequency: Int,
)
