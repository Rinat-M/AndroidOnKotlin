package com.rino.moviedb.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rino.moviedb.BuildConfig
import com.rino.moviedb.databinding.NowPlayingMovieItemBinding
import com.rino.moviedb.databinding.UpcomingMovieItemBinding
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.entities.MoviesCategory
import com.rino.moviedb.utils.processFavorite
import com.rino.moviedb.utils.toString

class MoviesAdapter(
    context: Context,
    private val movies: List<Movie>,
    private val moviesCategory: MoviesCategory,
    private val onItemClickListener: OnItemClickListener,
    private val onFavoriteClickListener: OnFavoriteClickListener
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private val circularProgressDrawable by lazy {
        CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
    }

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

            Glide.with(moviePoster.context)
                .load("${BuildConfig.IMAGE_TMDB_BASE_URL}${BuildConfig.IMAGE_TMDB_RELATIVE_PATH}${movie.posterPath}")
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(moviePoster)

            movieCard.setOnClickListener { onItemClickListener.onItemClick(movie) }

            favorite.processFavorite(movie.isFavorite ?: false)
            favorite.setOnClickListener {
                movie.isFavorite = !(movie.isFavorite ?: false)
                favorite.processFavorite(movie.isFavorite ?: false)

                onFavoriteClickListener.onFavorite(movie, movie.isFavorite ?: false)
            }
        }
    }

    inner class UpcomingMovieViewHolder(
        private val binding: UpcomingMovieItemBinding
    ) : MovieViewHolder(binding.root) {

        override fun bind(movie: Movie) = with(binding) {
            movieTitle.text = movie.title
            movieReleaseDate.text = movie.releaseDate.toString("yyyy-MM-dd")

            Glide.with(moviePoster.context)
                .load("${BuildConfig.IMAGE_TMDB_BASE_URL}${BuildConfig.IMAGE_TMDB_RELATIVE_PATH}${movie.posterPath}")
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(moviePoster)

            movieCard.setOnClickListener { onItemClickListener.onItemClick(movie) }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(movie: Movie)
    }

    interface OnFavoriteClickListener {
        fun onFavorite(movie: Movie, isFavorite: Boolean)
    }
}
