package com.rino.messagereceiver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.NotificationChannel

import android.app.NotificationManager
import android.content.Context

import android.os.Build
import com.rino.messagereceiver.receivers.MessageReceiver.Companion.CHANNEL_ID


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNotificationChannel()
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_LOW

            val channel =
                NotificationChannel(CHANNEL_ID, getString(R.string.broadcast_receiver), importance)

            notificationManager.createNotificationChannel(channel)
        }

    }
}