package com.example.wificontrol.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.wificontrol.MainActivity
import com.example.wificontrol.R
import com.example.wificontrol.components.INTENT_COMMAND
import com.example.wificontrol.components.INTENT_COMMAND_ACHIEVE
import com.example.wificontrol.components.INTENT_COMMAND_EXIT
import com.example.wificontrol.components.INTENT_COMMAND_REPLY

private const val NOTIFICATION_CHANNEL_GENERAL = "Message"
private const val CODE_FOREGROUND_SERVICE = 1
private const val CODE_REPLY_INTENT = 2
private const val CODE_ACHIEVE_INTENT = 3

class NotificationsService : Service() {
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()

//        registerMediaReceiver()
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val command = intent?.getStringExtra(INTENT_COMMAND)
        val notification = createForegroundNotification()
        startForeground(CODE_FOREGROUND_SERVICE, notification)
        when (command) {
            INTENT_COMMAND_EXIT -> {
                stopService()
                return START_NOT_STICKY
            }

            "NEW_MESSAGE" -> {
                val messageContent = intent.getStringExtra("message_content") ?: "Новое сообщение"
                showNewMessageNotification(messageContent)
            }
        }
        if (command == INTENT_COMMAND_REPLY) {
            Toast.makeText(this, "Clicked in Notification", Toast.LENGTH_LONG).show()
        }
        return START_STICKY
    }

    private fun stopService() {
        stopForeground(true)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_GENERAL,
                "WiFi Control",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "WiFi Control"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("ForegroundServiceType")
    private fun showNewMessageNotification(message: String) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_GENERAL)
            .setSmallIcon(R.drawable.ic_menu)
            .setContentTitle("Сообщение от тех.поддержки")
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(CODE_FOREGROUND_SERVICE, notification)
    }

    private fun createForegroundNotification(): Notification {
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_GENERAL)
            .setSmallIcon(R.drawable.ic_menu)
            .setContentTitle("WiFi Control")
            .setContentText("Новое сообщение")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        return notification
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService()
    }

    override fun onBind(p0: Intent?): IBinder? = null
}