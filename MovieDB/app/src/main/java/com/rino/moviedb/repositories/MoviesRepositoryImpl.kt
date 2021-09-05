package com.rino.moviedb.repositories

import com.rino.moviedb.database.dao.MovieGetDao
import com.rino.moviedb.database.dao.MovieSetDao
import com.rino.moviedb.database.entites.*
import com.rino.moviedb.datasources.DataSource
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.entities.coreModel
import com.rino.moviedb.entities.dbModel
import com.rino.moviedb.remote.entites.MovieDetailsDTO
import com.rino.moviedb.remote.entites.PersonDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRepositoryImpl(
    private val dataSource: DataSource,
    private val movieSetDao: MovieSetDao,
    private val movieGetDao: MovieGetDao
) : MoviesRepository {

    override fun getNowPlayingMovies(): Result<List<Movie>> = dataSource.getNowPlayingMovies()

    override fun getUpcomingMovies(): Result<List<Movie>> = dataSource.getUpcomingMovies()

    override fun getMovieDetails(movieId: Long): Result<MovieDetailsDTO?> =
        dataSource.getMovieDetails(movieId)

    override fun saveMovie(movie: Movie) = movieSetDao.insertMovie(movie.dbModel)

    override fun saveMovieToHistory(movieId: Long) {
        val history = History(movieId = movieId)
        movieSetDao.insertToHistory(history)
    }

    override fun getHistoryWithMovies() = movieGetDao.getHistoryWithMovies()

    override fun getMovieById(id: Long): Movie = movieGetDao.getMovieById(id).coreModel

    override fun saveNote(note: Note) = movieSetDao.insertNote(note)

    override fun getMovieExtendedById(id: Long): MovieExtended? =
        movieGetDao.getMovieExtendedById(id)

    override fun addMovieToFavorite(favorite: Favorite) = movieSetDao.insertToFavourite(favorite)

    override fun removeMovieFromFavorite(movieId: Long) =
        movieSetDao.deleteFromFavouriteByMovieId(movieId)

    override fun getAllFavoritesMoviesIds(): List<Long> = movieGetDao.getAllFavoritesMoviesIds()

    override fun getFavoritesMoviesFlow(): Flow<List<Movie>> =
        movieGetDao.getFavoritesMoviesFlow()
            .map { moviesDb ->
                moviesDb.map {
                    it.coreModel.apply { isFavorite = true }
                }
            }

    override fun insertMovieActors(movieId: Long, actors: List<Actor>) =
        movieSetDao.insertMovieActors(movieId, actors)

    override fun getPerson(personId: Long): Result<PersonDTO?> = dataSource.getPerson(personId)

}