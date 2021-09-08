package com.rino.moviedb.ui.map

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(context: Context) : ViewModel() {
    companion object {
        private const val TAG = "MapViewModel"
    }

    private val geoCoder: Geocoder by lazy { Geocoder(context) }

    private val _markerOptions: MutableLiveData<MarkerOptions> = MutableLiveData()
    val markerOptions: LiveData<MarkerOptions> = _markerOptions

    private fun getLatLngFromLocationName(address: String?): LatLng? {
        val addresses = try {
            geoCoder.getFromLocationName(address, 1)
        } catch (e: Exception) {
            Log.e(TAG, "Error while geoCoder.getFromLocationName!", e)
            null
        }

        return if (addresses.isNullOrEmpty()) {
            null
        } else {
            LatLng(addresses[0].latitude, addresses[0].longitude)
        }
    }

    fun searchByLocationName(address: String?) = viewModelScope.launch(Dispatchers.IO) {
        getLatLngFromLocationName(address)?.let { defaultLatLng ->
            val markerOptions = MarkerOptions().position(defaultLatLng).title(address)
            _markerOptions.postValue(markerOptions)
        }
    }
}