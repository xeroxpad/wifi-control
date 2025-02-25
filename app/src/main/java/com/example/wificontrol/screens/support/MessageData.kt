package com.example.wificontrol.screens.support

import androidx.core.app.NotificationCompat.MessagingStyle.Message
import com.google.firebase.Timestamp

data class MessageData(
    val msgId: String = "",
    val senderId: String = "",
    val repliedMessage: Message? = null,
    val imageUrl: String = "",
    val fireUrl: String = "",
    val fileName: String = "",
    val fileSize: String = "",
    val vidUrl: String = "",
    val progress: String = "",
    val content: String = "",
    val time: Timestamp? = null,
    val forwarded: Boolean = false,
    val status: MessageStatus = MessageStatus.DELIVERED
)
