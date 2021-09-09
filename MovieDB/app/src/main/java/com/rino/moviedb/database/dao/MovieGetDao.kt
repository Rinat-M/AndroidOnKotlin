package com.rino.moviedb.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.rino.moviedb.database.entites.HistoryWithMovie
import com.rino.moviedb.database.entites.Movie
import com.rino.moviedb.database.entites.MovieExtended
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieGetDao {

    @Query("SELECT id, viewingDate, movieId FROM History ORDER BY viewingDate DESC")
    fun getHistoryWithMovies(): List<HistoryWithMovie>

    @Query("SELECT * FROM Movie WHERE id = :id")
    fun getMovieById(id: Long): Movie

    @Query(
        """
        SELECT
            M.*,
            ifnull(F.id, 0) as isFavorite 
        FROM Movie as M 
        LEFT JOIN Favorite as F
            ON M.id = F.movieId
        WHERE M.id = :id"""
    )
    fun getMovieExtendedById(id: Long): MovieExtended?

    @Query("SELECT movieId FROM Favorite")
    fun getAllFavoritesMoviesIds(): List<Long>

    @Query(
        """
        SELECT Movie.* FROM Movie
        INNER JOIN Favorite
            ON Movie.id = Favorite.movieId
        ORDER BY Favorite.addDate DESC
    """
    )
    fun getFavoritesMoviesFlow(): Flow<List<Movie>>

    @Query(
        """
        SELECT Movie.* 
        FROM Movie as Movie
        INNER JOIN Favorite as Favorite 
            ON Movie.id = Favorite.movieId
        WHERE Movie.id = :id
        LIMIT 1;
        """
    )
    fun getFavoriteMovieById(id: Long): Movie?
}