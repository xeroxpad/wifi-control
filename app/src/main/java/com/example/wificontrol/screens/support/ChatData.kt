package com.example.wificontrol.screens.support

data class ChatData(
    val chatId: String = "",
    val last: MessageData? = null,
    val user1: ChatUserData? = null,
    val user2: ChatUserData? = null,
)


