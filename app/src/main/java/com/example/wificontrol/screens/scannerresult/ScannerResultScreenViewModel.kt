package com.example.wificontrol.screens.scannerresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entities.WiFiNetwork
import com.example.domain.usecases.GetWiFiNetworkUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScannerResultScreenViewModel(
    private val getWiFiNetworkUseCase: GetWiFiNetworkUseCase,
) : ViewModel() {
    private val _wifiNetworks = MutableStateFlow<List<WiFiNetwork>>(emptyList())
    val wiFiNetworks: StateFlow<List<WiFiNetwork>> get() = _wifiNetworks

    fun scanWiFiNetworks() {
        viewModelScope.launch {
            getWiFiNetworkUseCase.execute().collect { result ->
                val uniqueNetworks = result
                    .filter { it.ssid.isNotBlank() }
                    .toSet()
                    .toList()
                _wifiNetworks.value = uniqueNetworks
            }
        }
    }
}