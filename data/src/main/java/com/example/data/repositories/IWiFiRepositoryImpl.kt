package com.example.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.net.MacAddress
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.domain.entities.Router
import com.example.domain.entities.WiFiNetwork
import com.example.domain.repositories.IWiFiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.Socket
import java.util.Collections
import java.util.concurrent.Executors

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
        val currentIp = getDeviceIpAddress(context)
        wifiManager.startScan()
        val scanResults = wifiManager.scanResults
        val currentNetworkScanResult = scanResults.find { result ->
            result.SSID == currentSsid && result.BSSID == currentBssid
        }
        val wifiNetwork = WiFiNetwork(
            ssid = currentSsid,
            macId = currentBssid,
            signalStrength = currentNetworkScanResult?.capabilities ?: "",
            frequency = connectionInfo.frequency,
            ip = currentIp
        )
        return listOf(wifiNetwork)
    }

    fun getDeviceIpAddress(context: Context): String? {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipInt = wifiInfo.ipAddress
        if (ipInt != 0) {
            return (ipInt and 0xFF).toString() + "." +
                    (ipInt shr 8 and 0xFF) + "." +
                    (ipInt shr 16 and 0xFF) + "." +
                    (ipInt shr 24 and 0xFF)
        }
        return null
    }

    override suspend fun scanLocalNetwork(): List<Router> {
        val subnet = getSubnetAddress()
        val executor = Executors.newFixedThreadPool(20)
        val scanResult = mutableListOf<Router>()

        for (i in 1..254) {
            val host = "$subnet.$i"
            executor.submit {
                try {
                    val address = InetAddress.getByName(host)
                    if (address.isReachable(100)) {
                        if (isRouterOrSwitch(host)) {
                            println("Found router or switch at IP: $host")
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        executor.shutdown()
        return scanResult
    }

    private fun getSubnetAddress(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            for (networkInterface in Collections.list(interfaces)) {
                val addresses = networkInterface.inetAddresses
                for (inetAddress in Collections.list(addresses)) {
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        val ip = inetAddress.hostAddress
                        return ip.substring(0, ip.lastIndexOf('.'))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "адрес не определён"
    }

    private fun isRouterOrSwitch(host: String): Boolean {
        val commonPorts = listOf(80, 443, 22, 23)
        for (port in commonPorts) {
            try {
                Socket().use { socket ->
                    socket.connect(InetSocketAddress(host, port), 100)
                    return true
                }
            } catch (e: IOException) {
            }
        }
        val macAddress = getMacAddress(host)
        if (macAddress != null) {
            val manufacturer = getManufacturerFromMac(macAddress)
            if (manufacturer != null && isKnownNetworkingManufacturer(manufacturer)) {
                return true
            }
        }
        return false
    }

    private fun getMacAddress(ip: String): String? {
        try {
            val process = Runtime.getRuntime().exec("arp -a $ip")
            process.inputStream.bufferedReader().useLines { lines ->
                for (line in lines) {
                    if (line.contains(ip)) {
                        val macRegex = "([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})".toRegex()
                        val matchResult = macRegex.find(line)
                        if (matchResult != null) {
                            return matchResult.value
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getManufacturerFromMac(mac: String): String? {
        val oui = mac.substring(0, 8).replace(":", "-").toUpperCase()
        return ouiDatabase[oui]
    }

    private val ouiDatabase = mapOf(
        "00-1A-2B" to "Cisco Systems",
        "00-1B-63" to "Hewlett Packard",
    )

    private fun isKnownNetworkingManufacturer(manufacturer: String): Boolean {
        val knownManufacturers =
            listOf("Cisco", "HP", "Netgear", "D-Link", "TP-Link", "Huawei", "Juniper")
        return knownManufacturers.any { manufacturer.contains(it, ignoreCase = true) }
    }
}