package com.rino.moviedb.repositories

import com.rino.moviedb.entities.Movie

interface MoviesRepository {

    fun getNowPlayingMovies(): List<Movie>

    fun getUpcomingMovies(): List<Movie>

}