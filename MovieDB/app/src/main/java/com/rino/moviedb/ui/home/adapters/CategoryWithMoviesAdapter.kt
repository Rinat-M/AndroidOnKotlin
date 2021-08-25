package com.rino.moviedb.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rino.moviedb.databinding.CategoryItemBinding
import com.rino.moviedb.entities.CategoryWithMovies

class CategoryWithMoviesAdapter(
    private val categoriesWithMovies: List<CategoryWithMovies>,
    private val onItemClickListener: MoviesAdapter.OnItemClickListener
) : RecyclerView.Adapter<CategoryWithMoviesAdapter.CategoryWithMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryWithMovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(layoutInflater, parent, false)

        return CategoryWithMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryWithMovieViewHolder, position: Int) {
        holder.bind(categoriesWithMovies[position])
    }

    override fun getItemCount(): Int = categoriesWithMovies.count()

    inner class CategoryWithMovieViewHolder(
        private val binding: CategoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryWithMovies: CategoryWithMovies) {
            with(binding) {
                categoryTitle.text = categoryWithMovies.categoryTitle
                moviesRecyclerview.adapter = MoviesAdapter(
                    itemView.context,
                    categoryWithMovies.movies,
                    categoryWithMovies.category,
                    onItemClickListener
                )
            }
        }

    }


}
