package com.rino.moviedb.entities

data class CategoryWithMovies(
    val category: MoviesCategory,
    val categoryTitle: String,
    val movies: List<Movie>
)