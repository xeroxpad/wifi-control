package com.example.wificontrol.screens.home

import androidx.lifecycle.ViewModel
import com.example.wificontrol.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeScreenViewModel() : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _devicesNotFound = MutableStateFlow(R.string.add_device)
    val devicesNotFound: StateFlow<Int> = _devicesNotFound

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    fun toggleDialog() {
        _showDialog.value = !_showDialog.value
    }
}