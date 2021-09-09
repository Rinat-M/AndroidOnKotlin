package com.rino.moviedb.services

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rino.moviedb.BuildConfig
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.helpers.NotificationHelper
import com.rino.moviedb.repositories.MoviesRepository
import com.rino.moviedb.ui.details.MovieDetailsFragment
import com.rino.moviedb.utils.toString
import org.koin.android.ext.android.inject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
        private const val PUSH_KEY_MOVIE_ID = "movieId"
    }

    private var messageId = 0

    private val movieRepository: MoviesRepository by inject()
    private val notificationHelper: NotificationHelper by inject()

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        // sendRegistrationToServer(token)
    }

    override fun handleIntent(intent: Intent) {
        Log.d(TAG, "handleIntent: ${intent.extras?.toString()}")
        super.handleIntent(intent)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived: remoteMessage = $remoteMessage")

        if (remoteMessage.data.isNullOrEmpty()) {
            Log.d(TAG, "onMessageReceived: We stop processing because remoteMessage.data is empty!")
            return
        }

        Log.d(TAG, "onMessageReceived: remoteMessage.data = ${remoteMessage.data}")
        val movieId = remoteMessage.data[PUSH_KEY_MOVIE_ID]

        if (!movieId.isNullOrEmpty()) {
            Log.d(TAG, "onMessageReceived: movieId = $movieId")

            val movie = movieRepository.getFavoriteMovieById(movieId.toLong())

            movie?.let {
                Log.d(TAG, "onMessageReceived: '${it.title}' is on the favorite list.")

                sendNotification(it)
            }
        }
    }

    private fun sendNotification(movie: Movie) {
        Log.d(TAG, "sendNotification: Sending notification...")

        val pendingIntent = MovieDetailsFragment.createDeepLink(this, movie.id)

        val builder = notificationHelper.createFavoriteMoviesNotification(
            title = movie.title,
            body = "Is on favorite list. Release date: ${movie.releaseDate.toString("yyyy-MM-dd")}",
            pendingIntent = pendingIntent
        )

        notificationHelper.setBigPictureStyle(
            builder,
            "${BuildConfig.IMAGE_TMDB_BASE_URL}${BuildConfig.IMAGE_TMDB_RELATIVE_PATH}${movie.backdropPath}",
            "${BuildConfig.IMAGE_TMDB_BASE_URL}${BuildConfig.IMAGE_TMDB_RELATIVE_PATH}${movie.posterPath}"
        )

        notificationHelper.notify(messageId++, builder)
    }
}