package com.rino.moviedb.receivers

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.rino.moviedb.R
import com.rino.moviedb.helpers.NotificationHelper
import com.rino.moviedb.ui.MainActivity
import com.rino.moviedb.utils.showToast

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "GeofenceBroadcastReceiver"
    }

    private var messageId = 0

    override fun onReceive(context: Context?, intent: Intent) {
        Log.d(TAG, "onReceive event")

        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, "onReceive. Error receiving geofence event: $errorMessage")
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT
        ) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            // Get the transition details as a String.
            val geofenceTransitionDetails =
                getGeofenceTransitionDetails(geofenceTransition, triggeringGeofences)

            // Send notification and log the transition details.
            sendNotification(context, geofenceTransitionDetails)

            Log.i(TAG, geofenceTransitionDetails)
            context?.showToast(geofenceTransitionDetails)
        } else {
            // Log the error.
            context?.let {
                Log.e(
                    TAG,
                    it.getString(
                        R.string.geofence_transition_invalid_type,
                        geofenceTransition.toString()
                    )
                )
            }
        }
    }

    private fun getGeofenceTransitionDetails(
        geofenceTransition: Int,
        triggeringGeofences: List<Geofence>
    ): String {
        val trigger = when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> "Enter"
            Geofence.GEOFENCE_TRANSITION_EXIT -> "Exit"
            Geofence.GEOFENCE_TRANSITION_DWELL -> "Dwell"

            else -> "Unknown"
        }
        val geofences = triggeringGeofences.joinToString { it.requestId }
        return "Geofence was triggered by event $trigger: $geofences"
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(context: Context?, message: String) {
        context?.let {
            val notificationHelper = NotificationHelper(it)

            val intent = Intent(it, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(it, 555, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val builder = notificationHelper.createGeofenceNotification(
                "Geofence event",
                message,
                pendingIntent = pendingIntent
            )

            notificationHelper.notify(messageId++, builder)
        }
    }
}