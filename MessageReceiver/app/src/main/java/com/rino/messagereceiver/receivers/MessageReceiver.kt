package com.rino.messagereceiver.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.rino.messagereceiver.R

class MessageReceiver : BroadcastReceiver() {

    companion object {
        private const val NAME_MESSAGE = "MESSAGE"
        private const val TAG = "MessageReceiver"
        const val CHANNEL_ID = "message_receiver_channel"
    }

    private var messageId = 0

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra(NAME_MESSAGE) ?: ""
        Log.d(TAG, message)

        context?.let {
            val builder = NotificationCompat.Builder(it, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(it.getString(R.string.broadcast_receiver))
                .setContentTitle(message)

            val notificationManager =
                it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(messageId++, builder.build())
        }
    }
}