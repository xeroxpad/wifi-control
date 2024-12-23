package com.example.wificontrol.screens.profile


import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.api.VkApiFactory
import com.example.data.api.VkApiService
import com.example.data.mappers.ProfileInfoMapper
import com.example.domain.entities.ProfileInfo
import com.example.domain.usecases.RebootRouterUseCase
import com.example.wificontrol.screens.authorization.AuthState
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.launch

class ProfileScreenViewModel(application: Application): AndroidViewModel(application) {

    private val initialState = ProfileScreenState.Initial
    private val _profileState = MutableLiveData<ProfileScreenState>(initialState)
    private val mapperProfileInfo = ProfileInfoMapper()
    private val _profile = MutableLiveData<ProfileInfo>()
    val profile: LiveData<ProfileInfo> = _profile

    init {
        loadProfileInfo()
    }

    private fun loadProfileInfo() {
        viewModelScope.launch {
            val storage = VKPreferencesKeyValueStorage(getApplication())
            val token = VKAccessToken.restore(storage) ?: return@launch
            val response = VkApiFactory.apiService.loadProfileInfo(token.accessToken)
            val profileInfo = mapperProfileInfo.mapResponseToProfileInfo(response)
            _profileState.value = ProfileScreenState.Profile(info = profileInfo)
            _profile.value = profileInfo
        }
    }
}