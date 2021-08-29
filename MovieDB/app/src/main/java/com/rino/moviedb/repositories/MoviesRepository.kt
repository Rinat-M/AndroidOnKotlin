package com.rino.moviedb.repositories

import com.rino.moviedb.database.entites.HistoryWithMovie
import com.rino.moviedb.entities.Movie

interface MoviesRepository {

    fun getNowPlayingMovies(): Result<List<Movie>>

    fun getUpcomingMovies(): Result<List<Movie>>

    fun saveMovie(movie: Movie)

    fun saveMovieToHistory(movieId: Long)

    fun getHistoryWithMovies(): List<HistoryWithMovie>
}