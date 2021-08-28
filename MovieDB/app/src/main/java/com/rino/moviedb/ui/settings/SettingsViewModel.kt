package com.rino.moviedb.ui.settings

import androidx.lifecycle.ViewModel
import com.rino.moviedb.wrappers.MainSharedPreferencesWrapper

class SettingsViewModel(
    private val mainPreferences: MainSharedPreferencesWrapper
) : ViewModel() {

    var isAdultContentEnabled: Boolean
        get() = mainPreferences.isAdultContentEnabled
        set(value) {
            mainPreferences.isAdultContentEnabled = value
        }

}