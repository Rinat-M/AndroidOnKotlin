package com.rino.moviedb.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rino.moviedb.databinding.ActorItemBinding
import com.rino.moviedb.entities.Actor

class ActorsAdapter(
    private val actors: List<Actor>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ActorsAdapter.ActorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val binding = ActorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(actors[position])
    }

    override fun getItemCount(): Int = actors.size

    inner class ActorViewHolder(
        val binding: ActorItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(actor: Actor) {
            with(binding.actorName) {
                text = actor.name
                setOnClickListener { onItemClickListener.onItemClick(actor.id) }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(actorId: Long)
    }
}