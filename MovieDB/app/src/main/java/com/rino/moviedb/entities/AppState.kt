package com.rino.moviedb.entities

sealed class AppState {
    data class Success(val nowPlaying: List<Movie>, val upcomingMovies: List<Movie>) : AppState()
    object Loading : AppState()
    data class Error(val error: Throwable) : AppState()
}
