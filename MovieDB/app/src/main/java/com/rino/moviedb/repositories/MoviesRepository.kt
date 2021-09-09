package com.rino.moviedb.repositories

import com.rino.moviedb.database.entites.*
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.remote.entites.MovieDetailsDTO
import com.rino.moviedb.remote.entites.PersonDTO
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getNowPlayingMovies(): Result<List<Movie>>

    fun getUpcomingMovies(): Result<List<Movie>>

    fun getMovieDetails(movieId: Long): Result<MovieDetailsDTO?>

    fun saveMovie(movie: Movie)

    fun saveMovieToHistory(movieId: Long)

    fun getHistoryWithMovies(): List<HistoryWithMovie>

    fun getMovieById(id: Long): Movie

    fun saveNote(note: Note)

    fun getMovieExtendedById(id: Long): MovieExtended?

    fun addMovieToFavorite(favorite: Favorite)

    fun removeMovieFromFavorite(movieId: Long)

    fun getAllFavoritesMoviesIds(): List<Long>

    fun getFavoritesMoviesFlow(): Flow<List<Movie>>

    fun insertMovieActors(movieId: Long, actors: List<Actor>)

    fun getPerson(personId: Long): Result<PersonDTO?>

    fun getFavoriteMovieById(movieId: Long): Movie?

}