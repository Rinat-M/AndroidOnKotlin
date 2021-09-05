package com.rino.moviedb.datasources

import com.rino.moviedb.entities.Movie
import com.rino.moviedb.entities.coreMovieModel
import com.rino.moviedb.remote.MovieDbService
import com.rino.moviedb.remote.entites.MovieDetailsDTO
import com.rino.moviedb.remote.entites.PersonDTO

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

    override fun getMovieDetails(movieId: Long): Result<MovieDetailsDTO?> {
        return try {
            val response = movieDbService.getMovieDetails(movieId).execute()

            if (!response.isSuccessful) {
                return Result.failure(Exception("Response code: ${response.code()}. " +
                        "Response message: ${response.errorBody()?.string()}"))
            }

            val movieDetailsDTO = response.body()
            Result.success(movieDetailsDTO)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override fun getPerson(personId: Long): Result<PersonDTO?> {
        return try {
            val response = movieDbService.getPerson(personId).execute()

            if (!response.isSuccessful) {
                return Result.failure(Exception("Response code: ${response.code()}. " +
                        "Response message: ${response.errorBody()?.string()}"))
            }

            val personDTO = response.body()
            Result.success(personDTO)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}