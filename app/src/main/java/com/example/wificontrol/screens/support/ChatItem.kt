package com.example.wificontrol.screens.support

sealed class ChatItem {
    data class Message(val message: MessageData) : ChatItem()
    data class DateSeparator(val date: String) : ChatItem()
}