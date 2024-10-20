package com.example.data.repositories

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import com.example.domain.entities.Router
import com.example.domain.entities.WiFiNetwork
import com.example.domain.repositories.IWiFiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections

class IWiFiRepositoryImpl(private val context: Context): IWiFiRepository {
    @SuppressLint("MissingPermission")
    override suspend fun getAvailableNetworks(): List<WiFiNetwork> {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }
        wifiManager.startScan()
        val scanResult = wifiManager.scanResults

        return scanResult.map { result ->
            WiFiNetwork(
                ssid = result.SSID,
                macId = result.BSSID,
                signalStrength = result.capabilities,
                frequency = result.frequency
            )
        }
    }

    override suspend fun scanLocalNetwork(): List<Router> {
        val scanResults = mutableListOf<Router>()

        val localIpAddress = getLocalIpAddress()
        val subnet = getSubnetAddress(localIpAddress)

        withContext(Dispatchers.IO) {
            for (i in 1..254) {
                val host = "$subnet.$i"
                try {
                    val inetAddress = InetAddress.getByName(host)
                    if (inetAddress.isReachable(100)) {
                        val hostName = inetAddress.canonicalHostName
                        scanResults.add(Router(ip = host, macAddress = hostName))
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return scanResults
    }

    private fun getLocalIpAddress(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            for (networkInterface in Collections.list(interfaces)) {
                val addresses = networkInterface.inetAddresses
                for (inetAddress in Collections.list(addresses)) {
                    if (!inetAddress.isLoopbackAddress && inetAddress.hostAddress.indexOf(':') < 0) {
                        return inetAddress.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getSubnetAddress(ipAddress: String?): String {
        return ipAddress?.substringBeforeLast('.') ?: "192.168.1"
    }
}