package com.example.cinemaarchive.repository

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaarchive.R
import com.example.cinemaarchive.network.GlideApp
import com.example.cinemaarchive.network.GlideRequest
import com.example.cinemaarchive.repository.database.Database
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_film.view.*


class FilmRecyclerAdapter(
    private val inflater: LayoutInflater,
    private val items: List<Film>,
    private val itemClickListener: OnItemClickListener,
    private val likeClickListener: OnLikeClickListener,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val BASE_URL_IMG = "https://image.tmdb.org/t/p/w200"
    private val filmList: List<Film>? = null

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

        loadImage(items[position].filmPoster)?.into(holder.filmPosterIv)
    }

    private fun loadImage(@NonNull posterPath: String): GlideRequest<Drawable?>? {
        return GlideApp
            .with(context)
            .load(BASE_URL_IMG + posterPath)
            .centerCrop()
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

