package com.rino.moviedb.ui.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rino.moviedb.BuildConfig
import com.rino.moviedb.databinding.FavoriteItemBinding
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.utils.processFavorite
import com.rino.moviedb.utils.toString

class FavoritesAdapter(
    context: Context,
    private val movies: List<Movie>,
    private val onItemClickListener: OnItemClickListener,
    private val onFavoriteClickListener: OnFavoriteClickListener
) : ListAdapter<Movie, FavoritesAdapter.FavoriteViewHolder>(FavoritesDiffCallback()) {

    private val circularProgressDrawable by lazy {
        CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = FavoriteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) =
        holder.bind(movies[position])

    override fun getItemCount(): Int = movies.size

    inner class FavoriteViewHolder(
        private val binding: FavoriteItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) = with(binding) {
            movieTitle.text = movie.title
            movieReleaseDate.text = movie.releaseDate.toString("yyyy-MM-dd")
            moviePopularity.text = movie.popularity.toString()

            Glide.with(moviePoster.context)
                .load("${BuildConfig.IMAGE_TMDB_BASE_URL}${BuildConfig.IMAGE_TMDB_RELATIVE_PATH}${movie.posterPath}")
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(moviePoster)

            movieCard.setOnClickListener { onItemClickListener.onItemClick(movie.id) }

            favorite.processFavorite(movie.isFavorite ?: false)

            favorite.setOnClickListener {
                movie.isFavorite = !(movie.isFavorite ?: false)
                favorite.processFavorite(movie.isFavorite ?: false)

                onFavoriteClickListener.onFavorite(movie.id, movie.isFavorite ?: false)
            }
        }
    }

    class FavoritesDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
            oldItem == newItem

    }

    interface OnItemClickListener {
        fun onItemClick(movieId: Long)
    }

    interface OnFavoriteClickListener {
        fun onFavorite(movieId: Long, isFavorite: Boolean)
    }

}