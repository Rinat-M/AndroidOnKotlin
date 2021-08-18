package com.rino.moviedb.datasources

import com.rino.moviedb.entities.Movie

interface DataSource {

    fun getNowPlayingMovies(): Result<List<Movie>>

    fun getUpcomingMovies(): Result<List<Movie>>

}