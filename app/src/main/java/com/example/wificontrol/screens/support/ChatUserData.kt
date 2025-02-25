package com.example.wificontrol.screens.support

data class ChatUserData(
    val userId: String = "",
    val typing: Boolean = false,
    val bio: String = "",
    val username: String? = "",
    val ppurl: String = "",
    val email: String = "",
    val status: String = "",
    val unread: Int = 0,
)
