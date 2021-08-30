package com.rino.moviedb.repositories

import com.rino.moviedb.database.entites.Favorite
import com.rino.moviedb.database.entites.HistoryWithMovie
import com.rino.moviedb.database.entites.MovieWithNote
import com.rino.moviedb.database.entites.Note
import com.rino.moviedb.entities.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getNowPlayingMovies(): Result<List<Movie>>

    fun getUpcomingMovies(): Result<List<Movie>>

    fun saveMovie(movie: Movie)

    fun saveMovieToHistory(movieId: Long)

    fun getHistoryWithMovies(): List<HistoryWithMovie>

    fun getMovieById(id: Long): Movie

    fun saveNote(note: Note)

    fun getMovieWithNoteById(id: Long): MovieWithNote

    fun addMovieToFavorite(favorite: Favorite)

    fun removeMovieFromFavorite(movieId: Long)

    fun getAllFavoritesMoviesIds(): List<Long>

    fun getFavoritesMoviesFlow(): Flow<List<Movie>>

}