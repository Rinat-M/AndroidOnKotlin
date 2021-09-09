package com.rino.moviedb.helpers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.rino.moviedb.BuildConfig
import com.rino.moviedb.R

class NotificationHelper(base: Context) : ContextWrapper(base) {

    companion object {
        private const val TAG = "NotificationHelper"
    }

    private val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val smallIcon = R.mipmap.ic_launcher

    fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val geofenceChannel = getGeofenceChannel()
            geofenceChannel?.let {
                notificationManager.createNotificationChannel(it)
                Log.d(TAG, "GeofenceChannel created!")
            }

            val favoriteMoviesChannel = getFavoriteMoviesChannel()
            favoriteMoviesChannel?.let {
                notificationManager.createNotificationChannel(it)
                Log.d(TAG, "FavoriteMoviesChannel created!")
            }
        }
    }

    private fun getGeofenceChannel(): NotificationChannel? {
        Log.d(TAG, "getGeofenceChannel launched!")

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                this.getString(R.string.geofence_channel_id),
                this.getString(R.string.geofence_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description =
                    this@NotificationHelper.getString(R.string.geofence_channel_description)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                lightColor = Color.BLUE
                enableLights(true)
            }
        } else {
            null
        }

    }

    private fun getFavoriteMoviesChannel(): NotificationChannel? {
        Log.d(TAG, "getFavoriteMoviesChannel launched!")

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                this.getString(R.string.favorite_movies_channel_id),
                this.getString(R.string.favorite_movies_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = this@NotificationHelper
                    .getString(R.string.favorite_movies_channel_description)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                lightColor = Color.GREEN
                enableLights(true)
            }
        } else {
            null
        }
    }

    private fun createNotification(
        channelId: String,
        title: String,
        body: String,
        priority: Int,
        pendingIntent: PendingIntent? = null
    ): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(smallIcon)
            .setPriority(priority)
            .setAutoCancel(true)

        pendingIntent?.let { builder.setContentIntent(it) }

        return builder
    }

    fun createGeofenceNotification(
        title: String, body: String,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT,
        pendingIntent: PendingIntent? = null
    ) = createNotification(
        this.getString(R.string.geofence_channel_id),
        title,
        body,
        priority,
        pendingIntent
    )

    fun createFavoriteMoviesNotification(
        title: String, body: String,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT,
        pendingIntent: PendingIntent? = null
    ) = createNotification(
        this.getString(R.string.favorite_movies_channel_id),
        title,
        body,
        priority,
        pendingIntent
    )

    fun notify(id: Int, notification: NotificationCompat.Builder) =
        notificationManager.notify(id, notification.build())

    fun setBigPictureStyle(
        builder: NotificationCompat.Builder,
        bigPictureUrl: String,
        bigLargeIconUrl: String
    ) {
        val bigPicture = Glide.with(this)
            .asBitmap()
            .load(bigPictureUrl)
            .submit().get()

        val bigLargeIcon = Glide.with(this)
            .asBitmap()
            .load(bigLargeIconUrl)
            .submit().get()

        val bigPictureStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(bigPicture)
            .bigLargeIcon(null)

        builder.setStyle(bigPictureStyle).setLargeIcon(bigLargeIcon)
    }
}