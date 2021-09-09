package com.rino.moviedb

import android.app.Application
import com.rino.moviedb.di.appModule
import com.rino.moviedb.helpers.NotificationHelper
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(appModule)
        }

        NotificationHelper(this).createChannels()
    }
}