package com.example.wificontrol.screens.scannerresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entities.Router
import com.example.domain.entities.WiFiNetwork
import com.example.domain.usecases.GetWiFiNetworkUseCase
import com.example.domain.usecases.ScanLocalNetworkUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScannerResultScreenViewModel(
    private val getWiFiNetworkUseCase: GetWiFiNetworkUseCase,
    private val scanLocalNetworkUseCase: ScanLocalNetworkUseCase
) : ViewModel() {
    private val _wifiNetworks = MutableStateFlow<List<WiFiNetwork>>(emptyList())
    val wiFiNetworks: MutableStateFlow<List<WiFiNetwork>> get() = _wifiNetworks

    private val _router = MutableStateFlow<List<Router>>(emptyList())
    val router: StateFlow<List<Router>> = _router

    fun scanRouter() {
        viewModelScope.launch {
            scanLocalNetworkUseCase.execute().collect { result ->
                _router.value = result
            }
        }
    }

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