package com.example.a3edhomework.horses.presentation.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.a3edhomework.MainActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val fullName = intent.getStringExtra(EXTRA_FULL_NAME) ?: "Студент"
        val classTime = intent.getStringExtra(EXTRA_CLASS_TIME).orEmpty()

        showNotification(context, fullName, classTime)
    }

    private fun showNotification(context: Context, fullName: String, classTime: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannelIfNeeded(notificationManager)

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pending = PendingIntent.getActivity(
            context,
            PENDING_INTENT_ACTIVITY_CODE,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val contentText = if (classTime.isNotBlank()) {
            "$fullName, ваша пара начинается в $classTime."
        } else {
            "$fullName, ваша пара начинается сейчас."
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("⏰ Начало пары")
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pending)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannelIfNeeded(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = CHANNEL_DESCRIPTION
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    companion object {
        const val EXTRA_FULL_NAME = "extra_full_name"
        const val EXTRA_CLASS_TIME = "extra_class_time"

        const val CHANNEL_ID = "class_reminder_channel"
        const val CHANNEL_NAME = "Напоминания о парах"
        const val CHANNEL_DESCRIPTION = "Уведомления о начале пар по мобильной разработке"

        private const val NOTIFICATION_ID = 1001
        private const val PENDING_INTENT_ACTIVITY_CODE = 333
    }
}