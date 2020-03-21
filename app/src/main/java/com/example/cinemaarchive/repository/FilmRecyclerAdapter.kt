package com.example.cinemaarchive.repository

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaarchive.R
import com.example.cinemaarchive.network.loadImage
import com.example.cinemaarchive.repository.database.Database
import com.example.cinemaarchive.repository.database.Database.favoriteList
import com.google.android.material.snackbar.Snackbar

private const val ITEM = 0
private const val LOADING = 1

class FilmRecyclerAdapter(
    private val inflater: LayoutInflater,
    private val items: List<Film>,
    private val itemClickListener: OnItemClickListener,
    private val likeClickListener: OnLikeClickListener,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoadingFooterAdded = false

    init {
        for (film in items)
            markFavorites(film)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        when (viewType) {
            ITEM -> {
                val viewItem: View =
                    inflater.inflate(R.layout.item_film, parent, false)
                viewHolder = FilmViewHolder(viewItem)
            }
            LOADING -> {
                val viewLoading: View =
                    inflater.inflate(R.layout.item_loading, parent, false)
                viewHolder = LoadingViewHolder(viewLoading)
            }
        }
        return viewHolder as RecyclerView.ViewHolder
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder.itemViewType){
            ITEM -> {
                (holder as FilmViewHolder).bind(
                    items[position],
                    itemClickListener,
                    likeClickListener
                )
                loadImage(items[position].filmPoster, context)?.into(holder.filmPosterIv)
            }
            LOADING -> {
                (holder as LoadingViewHolder).mProgressBar.visibility = VISIBLE
            }

        }



    }
//TODO move snackBar above the bottomNavigation
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

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size - 1 && isLoadingFooterAdded) LOADING else ITEM
    }

    /*
        Helpers - Footer
   ___________________________________________________________________________________________
    */
    fun addLoadingFooter(): Unit {
        isLoadingFooterAdded = true
        add(Film())
    }

    fun removeLoadingFooter() {
        isLoadingFooterAdded = false
        val position: Int = items.size - 1
        (items as ArrayList<Film>).removeAt(position)
        notifyItemRemoved(position)
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

