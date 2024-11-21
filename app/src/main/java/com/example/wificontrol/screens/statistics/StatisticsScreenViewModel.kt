package com.example.wificontrol.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entities.SpeedTestResult
import com.example.domain.usecases.GetSpeedTestUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatisticsScreenViewModel(private val getSpeedTestUseCase: GetSpeedTestUseCase) :
    ViewModel() {
    private val _speedTestResult = MutableStateFlow<SpeedTestResult?>(null)
    val speedTestResult: StateFlow<SpeedTestResult?> = _speedTestResult

    fun fetchSpeedTest() {
        viewModelScope.launch {
            getSpeedTestUseCase.execute().collect { result ->
                val resultSpeedTest = result
                _speedTestResult.value = resultSpeedTest
            }
        }
    }
}