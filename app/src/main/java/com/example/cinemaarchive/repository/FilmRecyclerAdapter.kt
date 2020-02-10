package com.example.cinemaarchive.repository

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaarchive.R

class FilmRecyclerAdapter(
    private val inflater: LayoutInflater,
    private val items: List<Film>,
    private val itemClickListener: OnItemClickListener,
    private val likeClickListener: OnLikeClickListener
) : RecyclerView.Adapter<FilmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(
            inflater.inflate(
                R.layout.item_film,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(items[position], itemClickListener, likeClickListener)
    }

    interface OnItemClickListener {
        fun onItemClicked(film: Film)
    }

    interface OnLikeClickListener {
        fun onLikeClicked(film: Film, position: Int)
    }
}

