package com.rino.moviedb.repositories

import com.rino.moviedb.database.dao.MovieGetDao
import com.rino.moviedb.database.dao.MovieSetDao
import com.rino.moviedb.database.entites.History
import com.rino.moviedb.database.entites.MovieWithNote
import com.rino.moviedb.database.entites.Note
import com.rino.moviedb.datasources.DataSource
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.entities.coreModel
import com.rino.moviedb.entities.dbModel

class MoviesRepositoryImpl(
    private val dataSource: DataSource,
    private val movieSetDao: MovieSetDao,
    private val movieGetDao: MovieGetDao
) : MoviesRepository {

    override fun getNowPlayingMovies(): Result<List<Movie>> = dataSource.getNowPlayingMovies()

    override fun getUpcomingMovies(): Result<List<Movie>> = dataSource.getUpcomingMovies()

    override fun saveMovie(movie: Movie) = movieSetDao.insertMovie(movie.dbModel)

    override fun saveMovieToHistory(movieId: Long) {
        val history = History(movieId = movieId)
        movieSetDao.insertToHistory(history)
    }

    override fun getHistoryWithMovies() = movieGetDao.getHistoryWithMovies()

    override fun getMovieById(id: Long): Movie = movieGetDao.getMovieById(id).coreModel

    override fun saveNote(note: Note) = movieSetDao.insertNote(note)

    override fun getMovieWithNoteById(id: Long): MovieWithNote =
        movieGetDao.getMovieWithNoteById(id)

}