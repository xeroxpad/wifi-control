package com.example.wificontrol.screens.profile


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.RebootRouterUseCase
import kotlinx.coroutines.launch

class ProfileScreenViewModel(private val rebootRouterUseCase: RebootRouterUseCase): ViewModel() {
    var uiState by mutableStateOf<UiState>(UiState.Idle)
        private set

    fun rebootRouter() {
        viewModelScope.launch {
            uiState = UiState.Loading
            val result = rebootRouterUseCase()
            uiState = if (result.isSuccess) {
                UiState.Success("Router rebooted successfully!")
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val message: String) : UiState()
        data class Error(val errorMessage: String) : UiState()
    }
}