package com.rino.moviedb.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rino.moviedb.BuildConfig
import com.rino.moviedb.database.entites.HistoryWithMovie
import com.rino.moviedb.databinding.HistoryItemBinding
import com.rino.moviedb.utils.toString

class HistoryAdapter(
    context: Context,
    private val items: List<HistoryWithMovie>,
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<HistoryWithMovie, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    private val circularProgressDrawable by lazy {
        CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HistoryItemBinding.inflate(layoutInflater, parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class HistoryViewHolder(
        val binding: HistoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HistoryWithMovie) {
            with(binding) {
                val title = "${item.movie.title} (${item.movie.releaseDate.toString("yyyy")})"
                movieTitle.text = title

                viewingDate.text = item.viewingDate.toString("yyyy-MM-dd HH:mm:ss")

                Glide.with(moviePoster.context)
                    .load("${BuildConfig.IMAGE_TMDB_BASE_URL}${BuildConfig.IMAGE_TMDB_RELATIVE_PATH}${item.movie.posterPath}")
                    .centerCrop()
                    .placeholder(circularProgressDrawable)
                    .into(moviePoster)

                rootLayout.setOnClickListener { onItemClickListener.onItemClick(item.movieId) }
            }
        }

    }

    private class HistoryDiffCallback : DiffUtil.ItemCallback<HistoryWithMovie>() {
        override fun areItemsTheSame(
            oldItem: HistoryWithMovie,
            newItem: HistoryWithMovie
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: HistoryWithMovie,
            newItem: HistoryWithMovie
        ): Boolean = oldItem == newItem

    }

    interface OnItemClickListener {
        fun onItemClick(movieId: Long)
    }

}