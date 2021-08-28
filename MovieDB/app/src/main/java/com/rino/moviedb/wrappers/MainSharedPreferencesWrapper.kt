package com.rino.moviedb.wrappers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class MainSharedPreferencesWrapper(context: Context) {

    companion object {
        private const val MAIN_SETTINGS = "MAIN_SETTINGS"
        private const val ADULT_CONTENT_SETTINGS = "ADULT_CONTENT_SETTINGS"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(MAIN_SETTINGS, Context.MODE_PRIVATE)

    var isAdultContentEnabled: Boolean
        get() = sharedPreferences.getBoolean(ADULT_CONTENT_SETTINGS, false)
        set(value) {
            sharedPreferences.edit {
                putBoolean(ADULT_CONTENT_SETTINGS, value)
            }
        }
}