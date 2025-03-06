package com.example.wificontrol.screens.support

data class ChatData(
    val chatId: String = "",
    val last: MessageData? = null,
    val participants: List<String> = emptyList(),
)


