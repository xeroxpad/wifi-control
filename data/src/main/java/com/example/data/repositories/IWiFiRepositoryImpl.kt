package com.example.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import com.example.domain.entities.WiFiNetwork
import com.example.domain.repositories.IWiFiRepository

class IWiFiRepositoryImpl(private val context: Context): IWiFiRepository {
    @SuppressLint("MissingPermission")
    override fun getAvailableNetworks(): List<WiFiNetwork> {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }
        wifiManager.startScan()
        val scanResult = wifiManager.scanResults

        return scanResult.map { result  ->
            WiFiNetwork(
                ssid = result.SSID,
                macId = result.BSSID,
                signalStrength = result.capabilities,
                frequency = result.frequency
            )
        }
    }
}