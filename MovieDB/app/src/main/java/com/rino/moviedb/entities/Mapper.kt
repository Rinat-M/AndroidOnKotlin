package com.rino.moviedb.entities

typealias movieDb = com.rino.moviedb.database.entites.Movie

val Movie.dbModel
    get() = movieDb(
        this.id,
        this.posterPath,
        this.adult,
        this.overview,
        this.releaseDate,
        this.originalTitle,
        this.originalLanguage,
        this.title,
        this.backdropPath,
        this.popularity,
        this.voteCount,
        this.video,
        this.voteAverage
    )