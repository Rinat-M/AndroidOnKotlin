package com.rino.moviedb.rest_entites

import com.rino.moviedb.entities.Movie

data class NowPlayingMoviesDTO(
    val page: Int,
    val results: List<Movie>
)