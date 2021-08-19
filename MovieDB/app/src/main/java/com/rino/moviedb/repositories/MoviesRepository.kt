package com.rino.moviedb.repositories

import com.rino.moviedb.entities.Movie

interface MoviesRepository {

    fun getNowPlayingMovies(): Result<List<Movie>>

    fun getUpcomingMovies(): Result<List<Movie>>

}