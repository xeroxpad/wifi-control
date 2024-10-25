package com.example.domain.entities

data class WiFiNetwork(
    val ssid: String,
//    val ip: String? = null,
    val macId: String,
    val signalStrength: String,
    val frequency: Int,
    val ipAddress: String,
    val gateway: String,
    val dns1: String,
    val dns2: String,
    val serverAddress: String,
    val netmask: String
)
