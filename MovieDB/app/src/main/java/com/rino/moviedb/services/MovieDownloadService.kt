package com.rino.moviedb.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.repositories.MoviesRepository
import com.rino.moviedb.ui.home.HomeWithServiceFragment.Companion.HOME_WITH_SERVICE_INTENT_FILTER
import com.rino.moviedb.ui.home.HomeWithServiceFragment.Companion.NOW_PLAYING_MOVIES_PARAMETER
import com.rino.moviedb.ui.home.HomeWithServiceFragment.Companion.UPCOMING_MOVIES_PARAMETER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MovieDownloadService : Service() {

    companion object {
        fun start(context: Context) {
            val serviceIntent = Intent(context, MovieDownloadService::class.java)
            context.startService(serviceIntent)
        }

        fun stop(context: Context) {
            val serviceIntent = Intent(context, MovieDownloadService::class.java)
            context.stopService(serviceIntent)
        }
    }

    private val uiScope = MainScope()

    private val broadcastIntent = Intent(HOME_WITH_SERVICE_INTENT_FILTER)

    private val moviesRepository: MoviesRepository by inject()

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        uiScope.launch(Dispatchers.IO) {
            var nowPlayingMovies = listOf<Movie>()
            moviesRepository.getNowPlayingMovies()
                .onSuccess { nowPlayingMovies = it }
                .onFailure {
                    Log.e("MovieDownloadService", "Error while getNowPlayingMovies()", it)
                    nowPlayingMovies = listOf()
                }

            var upcomingMovies = listOf<Movie>()
            moviesRepository.getUpcomingMovies()
                .onSuccess { upcomingMovies = it }
                .onFailure {
                    Log.e("MovieDownloadService", "Error while getUpcomingMovies()", it)
                    upcomingMovies = listOf()
                }

            broadcastIntent.putParcelableArrayListExtra(
                NOW_PLAYING_MOVIES_PARAMETER,
                nowPlayingMovies as ArrayList
            )
            broadcastIntent.putParcelableArrayListExtra(
                UPCOMING_MOVIES_PARAMETER,
                upcomingMovies as ArrayList
            )

            LocalBroadcastManager
                .getInstance(this@MovieDownloadService)
                .sendBroadcast(broadcastIntent)
        }

        return START_STICKY
    }
}