package com.example.wificontrol.screens.profile

import com.example.domain.entities.ProfileInfo

sealed class ProfileScreenState {
    object Initial: ProfileScreenState()
    data class Profile(val info: ProfileInfo) : ProfileScreenState()
}