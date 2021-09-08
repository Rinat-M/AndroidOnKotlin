package com.rino.moviedb.remote

import com.rino.moviedb.remote.entites.MovieDetailsDTO
import com.rino.moviedb.remote.entites.MoviesDTO
import com.rino.moviedb.remote.entites.PersonDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDbService {

    @GET("3/movie/now_playing")
    fun getNowPlayingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<MoviesDTO>

    @GET("3/movie/upcoming")
    fun getUpcomingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<MoviesDTO>

    @GET("3/movie/{movieId}")
    fun getMovieDetails(
        @Path("movieId") movieId: Long,
        @Query("language") language: String = "en-US",
        @Query("append_to_response") appendToResponse: String = "credits"
    ): Call<MovieDetailsDTO>

    @GET("3/person/{personId}")
    fun getPerson(
        @Path("personId") personId: Long,
        @Query("language") language: String = "en-US"
    ): Call<PersonDTO>

}