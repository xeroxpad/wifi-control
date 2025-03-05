package com.example.wificontrol.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.wificontrol.MainActivity
import com.example.wificontrol.R
import com.example.wificontrol.components.NOTIFICATION_CHANNEL_GENERAL
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingNotifications: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Проверяем, есть ли данные в сообщении (например, заголовок и текст чата)
        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"] ?: "Новое сообщение"
            val body = remoteMessage.data["body"] ?: "Вам пришло сообщение в чат"
            showNotification(title, body)
        }
    }

    // Метод для отображения уведомления
    private fun showNotification(title: String, body: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "chat_notifications"

        // Создаем канал уведомлений для Android 8.0 и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Чат уведомления",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Intent для открытия активности при клике на уведомление
        val intent = Intent(this, MainActivity::class.java) // Замените на вашу активность
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Создаем уведомление
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Замените на вашу иконку
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true) // Уведомление исчезает после клика
            .setContentIntent(pendingIntent)

        // Показываем уведомление с уникальным ID (0)
        notificationManager.notify(0, notificationBuilder.build())
    }
}