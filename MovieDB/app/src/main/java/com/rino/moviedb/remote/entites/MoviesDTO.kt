package com.rino.moviedb.remote.entites

import com.rino.moviedb.entities.Movie

data class MoviesDTO(
    val page: Int,
    val results: List<Movie>
)