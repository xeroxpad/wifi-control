package com.example.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.net.DhcpInfo
import android.net.wifi.WifiManager
import android.text.format.Formatter
import com.example.domain.entities.WiFiNetwork
import com.example.domain.repositories.IWiFiRepository

@Suppress("DEPRECATION")
class IWiFiRepositoryImpl(private val context: Context) : IWiFiRepository {
    @SuppressLint("MissingPermission")
    override suspend fun getAvailableNetworks(): List<WiFiNetwork> {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }
        val connectionInfo = wifiManager.connectionInfo
        val currentSsid = connectionInfo.ssid.trim('"')
        val currentBssid = connectionInfo.bssid
        val dhcpInfo: DhcpInfo? = wifiManager.dhcpInfo

        wifiManager.startScan()
        val scanResults = wifiManager.scanResults
        val currentNetworkScanResult = scanResults.find { result ->
            result.SSID == currentSsid && result.BSSID == currentBssid
        }

        val dhcpInfoString = dhcpInfo?.let {
            "IP: ${Formatter.formatIpAddress(it.ipAddress)}\nШлюз: ${Formatter.formatIpAddress(it.gateway)}\nDNS1: ${
                Formatter.formatIpAddress(
                    it.dns1
                )
            }\nDNS2: ${Formatter.formatIpAddress(it.dns2)}\n" +
                    "IP сервера: ${Formatter.formatIpAddress(it.serverAddress)}\nМаска подсети: ${
                        Formatter.formatIpAddress(
                            it.netmask
                        )
                    }"
        } ?: "информация о DHCP недоступна."

        val wifiNetwork = WiFiNetwork(
            ssid = currentSsid,
            macId = currentBssid,
            signalStrength = currentNetworkScanResult?.capabilities ?: "информация не доступна",
            frequency = connectionInfo.frequency,
            ip = dhcpInfoString
        )
        return listOf(wifiNetwork)
    }
}