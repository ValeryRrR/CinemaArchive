package com.example.cinemaarchive.repository

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaarchive.R
import com.example.cinemaarchive.repository.database.Database
import com.google.android.material.snackbar.Snackbar


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

     fun onItemRemove(
        mAdapterPosition: Int,
        film: Film,
        view: View
    ) {
        val snackbar: Snackbar = Snackbar
            .make(
                view,
                R.string.remove_from_favorite,
                Snackbar.LENGTH_LONG
            )
            .setAction(R.string.undo) {
                film.isFavorite = true
                Database.favoriteList.add(mAdapterPosition, film)
                notifyItemInserted(mAdapterPosition)
            }
        snackbar.show()
    }

    interface OnItemClickListener {
        fun onItemClicked(film: Film)
    }

    interface OnLikeClickListener {
        fun onLikeClicked(film: Film, position: Int, isFavoriteChecked: Boolean)
    }
}

