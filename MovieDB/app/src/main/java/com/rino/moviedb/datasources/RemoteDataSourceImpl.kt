package com.rino.moviedb.datasources

import com.rino.moviedb.entities.Movie
import com.rino.moviedb.remote.MovieDbService

class RemoteDataSourceImpl(private val movieDbService: MovieDbService) : DataSource {

    override fun getNowPlayingMovies(): Result<List<Movie>> {
        return try {
            val response = movieDbService.getNowPlayingMovies().execute()

            if (!response.isSuccessful) {
                return Result.failure(Exception("Response code: ${response.code()}. " +
                        "Response message: ${response.errorBody()?.string()}"))
            }

            val moviesDto = response.body()
            Result.success(moviesDto?.results ?: listOf())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override fun getUpcomingMovies(): Result<List<Movie>> {
        return try {
            val response = movieDbService.getUpcomingMovies().execute()

            if (!response.isSuccessful) {
                return Result.failure(Exception("Response code: ${response.code()}. " +
                        "Response message: ${response.errorBody()?.string()}"))
            }

            val moviesDto = response.body()
            Result.success(moviesDto?.results ?: listOf())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}