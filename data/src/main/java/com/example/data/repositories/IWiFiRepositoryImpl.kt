package com.example.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import com.example.domain.entities.Router
import com.example.domain.entities.WiFiNetwork
import com.example.domain.repositories.IWiFiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileReader
import java.net.InetAddress

class IWiFiRepositoryImpl(private val context: Context): IWiFiRepository {
    @SuppressLint("MissingPermission")
    override suspend fun getAvailableNetworks(): List<WiFiNetwork> {
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

    override suspend fun scanLocalNetwork(): List<Router> {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val dhcpInfo = wifiManager.dhcpInfo
        val deviceIp = dhcpInfo.ipAddress
        val subnetMask = dhcpInfo.netmask
        val range = getNetworkRange(deviceIp, subnetMask)
        val reachableDevices = scanNetwork(range)
        return reachableDevices.mapNotNull { address ->
            val mac = address.hostAddress?.let { getMacAddressFromArp(it) }
            if (mac != null) {
                address.hostAddress?.let { Router(ip = it, macAddress = mac) }
            } else {
                null
            }
        }
    }

    private fun getNetworkRange(ip: Int, subnetMask: Int): Pair<Int, Int> {
        val networkAddress = ip and subnetMask
        val broadcastAddress = networkAddress or (subnetMask.inv())
        return Pair(networkAddress, broadcastAddress)
    }

    private suspend fun scanNetwork(range: Pair<Int, Int>): List<InetAddress> {
        val reachableDevices = mutableListOf<InetAddress>()
        for (ip in range.first until range.second) {
            val address = withContext(Dispatchers.IO) {
                InetAddress.getByName(intToIp(ip))
            }
            if (withContext(Dispatchers.IO) {
                    address.isReachable(1000)
                }) {
                reachableDevices.add(address)
            }
        }
        return reachableDevices
    }

    private fun getMacAddressFromArp(ip: String): String? {
        val bufferedReader = BufferedReader(FileReader("/proc/net/arp"))
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            val parts = line!!.split("\\s+".toRegex()).toTypedArray()
            if (parts[0] == ip) {
                return parts[3]
            }
        }
        return null
    }

    private fun intToIp(ip: Int): String {
        return "${ip and 0xFF}.${(ip shr 8) and 0xFF}.${(ip shr 16) and 0xFF}.${(ip shr 24) and 0xFF}"
    }
}