package com.rino.moviedb.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rino.moviedb.databinding.MovieItemBinding
import com.rino.moviedb.entities.Movie
import com.rino.moviedb.utils.toString

class NowPlayingMoviesAdapter(
    private val movies: List<Movie>
) : RecyclerView.Adapter<NowPlayingMoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(layoutInflater, parent, false)

        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.count()

    class MovieViewHolder(
        private val binding: MovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            with(binding) {
                movieTitle.text = movie.title
                movieReleaseDate.text = movie.releaseDate.toString("yyyy-MM-dd")
                moviePopularity.text = movie.popularity.toString()
            }
        }

    }


}
