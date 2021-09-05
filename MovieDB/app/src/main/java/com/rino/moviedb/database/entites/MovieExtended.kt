package com.rino.moviedb.database.entites

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieExtended(
    @Embedded val movie: Movie,
    var isFavorite: Boolean,
    @Relation(parentColumn = "id", entityColumn = "movieId")
    val note: Note? = null,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        entity = Actor::class,
        associateBy = Junction(
            MovieActorCrossRef::class,
            parentColumn = "movieId",
            entityColumn = "actorId"
        )
    )
    val actors: List<Actor>
)