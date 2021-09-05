package com.rino.moviedb.entities

import com.rino.moviedb.remote.entites.CastDTO
import com.rino.moviedb.remote.entites.MovieDetailsDTO

typealias movieDb = com.rino.moviedb.database.entites.Movie
typealias actorDb = com.rino.moviedb.database.entites.Actor

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
        this.voteAverage,
        this.budget,
        this.imdbId,
        this.revenue,
        this.tagline,
        this.director
    )

val movieDb.coreModel
    get() = Movie(
        this.id,
        this.posterPath,
        this.adult,
        this.overview,
        this.releaseDate,
        listOf(),
        this.originalTitle,
        this.originalLanguage,
        this.title,
        this.backdropPath,
        this.popularity,
        this.voteCount,
        this.video,
        this.voteAverage,
        false,
        this.budget,
        this.imdbId,
        this.revenue,
        this.tagline,
        this.director
    )

val MovieDetailsDTO.coreMovieModel
    get() = Movie(
        this.id,
        this.posterPath,
        this.adult,
        this.overview,
        this.releaseDate,
        this.genreIds.map { it.id },
        this.originalTitle,
        this.originalLanguage,
        this.title,
        this.backdropPath,
        this.popularity,
        this.voteCount,
        this.video,
        this.voteAverage,
        false,
        this.budget,
        this.imdbId,
        this.revenue,
        this.tagline
    )

val CastDTO.dbActorModel
    get() = actorDb(
        this.id,
        this.adult,
        this.gender,
        this.knownForDepartment,
        this.name,
        this.originalName,
        this.popularity,
        this.profilePath,
        this.castId,
        this.character,
        this.creditId,
        this.order
    )