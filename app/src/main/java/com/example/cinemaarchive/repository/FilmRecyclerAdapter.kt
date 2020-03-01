package com.example.cinemaarchive.repository

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaarchive.R
import com.example.cinemaarchive.network.BASE_URL_IMG
import com.example.cinemaarchive.network.GlideApp
import com.example.cinemaarchive.network.GlideRequest
import com.example.cinemaarchive.network.loadImage
import com.example.cinemaarchive.repository.database.Database
import com.example.cinemaarchive.repository.database.Database.favoriteList
import com.google.android.material.snackbar.Snackbar


class FilmRecyclerAdapter(
    private val inflater: LayoutInflater,
    private val items: List<Film>,
    private val itemClickListener: OnItemClickListener,
    private val likeClickListener: OnLikeClickListener,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        for (film in items)
            markFavorites(film)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmViewHolder(
            inflater.inflate(
                R.layout.item_film,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FilmViewHolder).bind(items[position], itemClickListener, likeClickListener)

        loadImage(items[position].filmPoster, context)?.into(holder.filmPosterIv)
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

    private fun markFavorites(film: Film){
        if (favoriteList.contains(film))
            film.isFavorite = true
    }

    /*
        Helpers - Pagination
   ___________________________________________________________________________________________
    */
    fun addAll(list: List<Film>) {
        for (film in list) {
            add(film)
        }
    }

    fun add(item: Film) {
        markFavorites(item)
        (items as ArrayList<Film>).add(item)
        notifyItemInserted(items.size - 1)
    }
}

