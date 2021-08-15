package com.rino.moviedb.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rino.moviedb.databinding.NowPlayingMovieItemBinding
import com.rino.moviedb.databinding.UpcomingMovieItemBinding
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.entities.MoviesCategory
import com.rino.moviedb.utils.toString

class MoviesAdapter(
    private val movies: List<Movie>,
    private val moviesCategory: MoviesCategory,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        when (moviesCategory) {
            MoviesCategory.NOW_PLAYING -> {
                val binding = NowPlayingMovieItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )

                NowPlayingMovieViewHolder(binding)
            }
            MoviesCategory.UPCOMING -> {
                val binding = UpcomingMovieItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )

                UpcomingMovieViewHolder(binding)
            }
        }

    override fun onBindViewHolder(holderNowPlaying: MovieViewHolder, position: Int) =
        holderNowPlaying.bind(movies[position])

    override fun getItemCount(): Int = movies.count()

    abstract class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(movie: Movie)
    }

    inner class NowPlayingMovieViewHolder(
        private val binding: NowPlayingMovieItemBinding
    ) : MovieViewHolder(binding.root) {

        override fun bind(movie: Movie) = with(binding) {
            movieTitle.text = movie.title
            movieReleaseDate.text = movie.releaseDate.toString("yyyy-MM-dd")
            moviePopularity.text = movie.popularity.toString()

            movieCard.setOnClickListener { onItemClickListener.onItemClick(movie) }
        }

    }

    inner class UpcomingMovieViewHolder(
        private val binding: UpcomingMovieItemBinding
    ) : MovieViewHolder(binding.root) {

        override fun bind(movie: Movie) = with(binding) {
            movieTitle.text = movie.title
            movieReleaseDate.text = movie.releaseDate.toString("yyyy-MM-dd")

            movieCard.setOnClickListener { onItemClickListener.onItemClick(movie) }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(movie: Movie)
    }
}
