package com.example.wificontrol.screens.scannerresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entities.WiFiNetwork
import com.example.domain.usecases.GetWiFiNetworkUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ScannerResultScreenViewModel(private val getWiFiNetworkUseCase: GetWiFiNetworkUseCase) : ViewModel() {
    private val _wifiNetworks = MutableStateFlow<List<WiFiNetwork>>(emptyList())
    val wiFiNetworks: MutableStateFlow<List<WiFiNetwork>> get() = _wifiNetworks

    fun scanWiFiNetworks() {
        viewModelScope.launch {
            getWiFiNetworkUseCase.execute().collect() { result ->
                _wifiNetworks.value = result
            }
        }
    }
}