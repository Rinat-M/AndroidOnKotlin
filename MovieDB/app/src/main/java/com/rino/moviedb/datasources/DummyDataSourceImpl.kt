package com.rino.moviedb.datasources

import com.rino.moviedb.entities.Movie
import com.rino.moviedb.utils.toDate
import java.util.*

class DummyDataSourceImpl : DataSource {
    override fun getNowPlayingMovies(): List<Movie> = listOf(
            Movie(1, "/poster.jpg", false, "Overview", Date(), listOf(), "Emma", "en", "Эмма", "", 6.9, 1, false, 4.41),
            Movie(2, "/poster.jpg", false, "Overview", Date(), listOf(), "Vivarium", "en", "Вивариум", "", 5.7, 1, false, 4.41),
            Movie(3, "/poster.jpg", false, "Overview", "2016-08-03".toDate("yyyy-MM-dd"), listOf(), "Suicide Squad", "en", "Suicide Squad", "", 5.91, 1, false, 4.41),
            Movie(4, "/poster.jpg", false, "Overview", "2016-07-27".toDate("yyyy-MM-dd"), listOf(), "Jason Bourne", "en", "Jason Bourne", "", 5.91, 1, false, 4.41),
        )

    override fun getUpcomingMovies(): List<Movie> = listOf(
            Movie(5, "/poster.jpg", false, "Overview", "2016-09-02".toDate("yyyy-MM-dd"), listOf(), "The Light Between Oceans", "en", "The Light Between Oceans", "", 4.546151, 1, false, 4.41),
            Movie(6, "/poster.jpg", false, "Overview", "2016-09-14".toDate("yyyy-MM-dd"), listOf(), "Keanu", "en", "Keanu", "", 3.51555, 1, false, 4.41),
            Movie(7, "/poster.jpg", false, "Overview", "2016-09-08".toDate("yyyy-MM-dd"), listOf(), "Sully", "en", "Sully", "", 3.254896, 1, false, 4.41),
            Movie(8, "/poster.jpg", false, "Overview", "2016-09-09".toDate("yyyy-MM-dd"), listOf(), "Guernika", "en", "Guernika", "", 3.218451, 1, false, 4.41),
        )
}