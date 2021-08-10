package com.rino.moviedb.datasources

import com.rino.moviedb.entities.Movie

interface DataSource {

    fun getNowPlayingMovies(): List<Movie>

    fun getUpcomingMovies(): List<Movie>

}