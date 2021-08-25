package com.rino.moviedb.datasources

import com.google.gson.Gson
import com.rino.moviedb.BuildConfig
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.remote.entites.MoviesDTO
import com.rino.moviedb.utils.getLines
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RemoteDataSourceOldImpl : DataSource {

    override fun getNowPlayingMovies(): Result<List<Movie>> {
        try {
            val url =
                URL("${BuildConfig.MOVIEDB_BASE_URL}/3/movie/now_playing?api_key=${BuildConfig.MOVIEDB_API_KEY}&language=en-US&page=1")

            lateinit var urlConnection: HttpsURLConnection

            return try {
                urlConnection = url.openConnection() as HttpsURLConnection

                if (urlConnection.responseCode != HttpsURLConnection.HTTP_OK) {
                    return Result.failure(Exception("Response code: ${urlConnection.responseCode}. Response message: ${urlConnection.responseMessage}"))
                }

                val lines = urlConnection.getLines()
                val nowPlayingMoviesDTO = Gson().fromJson(lines, MoviesDTO::class.java)

                Result.success(nowPlayingMoviesDTO.results)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            } finally {
                urlConnection.disconnect()
            }

        } catch (e: MalformedURLException) {
            return Result.failure(e)
        }

    }

    override fun getUpcomingMovies(): Result<List<Movie>> {
        try {
            val url =
                URL("${BuildConfig.MOVIEDB_BASE_URL}/3/movie/upcoming?api_key=${BuildConfig.MOVIEDB_API_KEY}&language=en-US&page=1")

            lateinit var urlConnection: HttpsURLConnection

            return try {
                urlConnection = url.openConnection() as HttpsURLConnection

                if (urlConnection.responseCode != HttpsURLConnection.HTTP_OK) {
                    return Result.failure(Exception("Response code: ${urlConnection.responseCode}. Response message: ${urlConnection.responseMessage}"))
                }

                val lines = urlConnection.getLines()
                val nowPlayingMoviesDTO = Gson().fromJson(lines, MoviesDTO::class.java)

                Result.success(nowPlayingMoviesDTO.results)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            return Result.failure(e)
        }
    }

}