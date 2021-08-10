package com.rino.moviedb.datasources

import com.rino.moviedb.entities.Movie
import java.util.*

class DummyDataSourceImpl : DataSource {
    override fun getNowPlayingMovies(): List<Movie> {
        return listOf(
            Movie(1, "/poster.jpg", false, "Overview", Date(), listOf(), "Emma", "en", "Эмма", "", 6.9, 1, false, 4.41),
            Movie(2, "/poster.jpg", false, "Overview", Date(), listOf(), "Vivarium", "en", "Вивариум", "", 5.7, 1, false, 4.41),
            Movie(3, "/poster.jpg", false, "Overview", Date("2016-08-03"), listOf(), "Suicide Squad", "en", "Suicide Squad", "", 5.91, 1, false, 4.41),
            Movie(4, "/poster.jpg", false, "Overview", Date("2016-07-27"), listOf(), "Jason Bourne", "en", "Jason Bourne", "", 5.91, 1, false, 4.41),
        )

    }

    override fun getUpcomingMovies(): List<Movie> {
        return listOf(
            Movie(5, "/poster.jpg", false, "Overview", Date("2016-09-02"), listOf(), "The Light Between Oceans", "en", "The Light Between Oceans", "", 4.546151, 1, false, 4.41),
            Movie(6, "/poster.jpg", false, "Overview", Date("2016-09-14"), listOf(), "Keanu", "en", "Keanu", "", 3.51555, 1, false, 4.41),
            Movie(7, "/poster.jpg", false, "Overview", Date("2016-09-08"), listOf(), "Sully", "en", "Sully", "", 3.254896, 1, false, 4.41),
            Movie(8, "/poster.jpg", false, "Overview", Date("2016-09-09"), listOf(), "Guernika", "en", "Guernika", "", 3.218451, 1, false, 4.41),
        )
    }
}