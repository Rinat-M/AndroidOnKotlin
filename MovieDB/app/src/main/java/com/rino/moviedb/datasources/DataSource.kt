package com.rino.moviedb.datasources

import com.rino.moviedb.entities.Movie
import com.rino.moviedb.remote.entites.MovieDetailsDTO
import com.rino.moviedb.remote.entites.PersonDTO

interface DataSource {

    fun getNowPlayingMovies(): Result<List<Movie>>

    fun getUpcomingMovies(): Result<List<Movie>>

    fun getMovieDetails(movieId: Long): Result<MovieDetailsDTO?>

    fun getPerson(personId: Long): Result<PersonDTO?>

}