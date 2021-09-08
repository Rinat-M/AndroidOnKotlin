package com.rino.moviedb.wrappers

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.rino.moviedb.receivers.GeofenceBroadcastReceiver
import com.rino.moviedb.utils.roundTo
import java.lang.Exception

class GeofencesWrapper(base: Context?) : ContextWrapper(base) {

    companion object {
        private const val GEOFENCE_EXPIRATION_IN_MILLISECONDS = 1_800_000L  // 30 min

        fun getErrorString(e: Exception): String? = if (e is ApiException) {
            GeofenceStatusCodes.getStatusCodeString(e.statusCode)
        } else {
            e.localizedMessage
        }
    }

    private val geofencingClient = LocationServices.getGeofencingClient(this)

    private val geofences: ArrayList<Geofence> = arrayListOf()

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createGeofencesFromList(entities: List<LatLng>, radius: Float) {
        entities.forEach { latLng ->
            geofences.add(
                Geofence.Builder()
                    .setRequestId("${latLng.latitude.roundTo(6)}:${latLng.longitude.roundTo(6)}")
                    .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setLoiteringDelay(5000)
                    .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .build()
            )
        }
    }

    private fun getGeofencingRequest(): GeofencingRequest =
        GeofencingRequest.Builder().apply {
            setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER)
            addGeofences(geofences)
        }.build()

    @SuppressLint("MissingPermission")
    fun addGeofences(entities: List<LatLng>, radius: Float): Task<Void>? {
        createGeofencesFromList(entities, radius)
        return geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent)
    }

    @SuppressLint("MissingPermission")
    fun removeGeofences(): Task<Void>? =
        geofencingClient.removeGeofences(geofencePendingIntent)


}