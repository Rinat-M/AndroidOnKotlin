package com.rino.moviedb.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rino.moviedb.R
import com.rino.moviedb.utils.showToast

class ConnectivityActionBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.showToast(R.string.connectivity_action)
    }
}