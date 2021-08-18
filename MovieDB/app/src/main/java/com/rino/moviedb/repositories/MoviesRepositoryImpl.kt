package com.rino.moviedb.repositories

import com.rino.moviedb.datasources.DataSource
import com.rino.moviedb.entities.Movie

class MoviesRepositoryImpl(private val dataSource: DataSource) : MoviesRepository {

    override fun getNowPlayingMovies(): Result<List<Movie>> = dataSource.getNowPlayingMovies()

    override fun getUpcomingMovies(): Result<List<Movie>> = dataSource.getUpcomingMovies()

}