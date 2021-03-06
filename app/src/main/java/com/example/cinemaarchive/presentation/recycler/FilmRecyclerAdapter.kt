package com.example.cinemaarchive.presentation.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaarchive.R
import com.example.cinemaarchive.data.network.loadImage
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.presentation.recycler.holders.FilmViewHolder
import com.example.cinemaarchive.presentation.recycler.holders.LoadingViewHolder


private const val ITEM = 0
private const val LOADING = 1

class FilmRecyclerAdapter(
    private var items: List<Film>,
    private val itemClickListener: (film: Film) -> Unit,
    private val likeClickListener: (film: Film, position: Int, isFavoriteChecked: Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoadingFooterAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        when (viewType) {
            ITEM -> {
                val viewItem: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
                viewHolder = FilmViewHolder(viewItem)
            }
            LOADING -> {
                val viewLoading: View =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_loading, parent, false)
                viewHolder = LoadingViewHolder(viewLoading)
            }
        }
        return viewHolder as RecyclerView.ViewHolder
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ITEM -> {
                (holder as FilmViewHolder).bind(
                    items[position],
                    itemClickListener,
                    likeClickListener
                )
                loadImage(items[position].filmPoster, holder.filmPosterIv.context)?.into(holder.filmPosterIv)
            }
            LOADING -> {
                (holder as LoadingViewHolder).mProgressBar.visibility = VISIBLE
            }
        }
    }

    fun onItemRemove(
        mAdapterPosition: Int,
        film: Film
    ) {
        (items as ArrayList<Film>).remove(film)
        notifyItemRemoved(mAdapterPosition)
    }

    fun onItemAdd(
        mAdapterPosition: Int,
        film: Film){
        (items as ArrayList<Film>).add(mAdapterPosition, film)
        notifyItemInserted(mAdapterPosition)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size - 1 && isLoadingFooterAdded) LOADING else ITEM
    }

    private var footerPosition = items.size
    fun addLoadingFooter() {
        isLoadingFooterAdded = true
        footerPosition = items.size
        (items as ArrayList<Film>).add(Film())
        notifyItemInserted(footerPosition)
    }

    fun removeLoadingFooter() {
        isLoadingFooterAdded = false
        (items as ArrayList<Film>).removeAt(footerPosition)
        notifyItemRemoved(footerPosition)
    }

    fun updateList(newList: List<Film>) {
        val diffCallback = DiffCallback(newList, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newList
        diffResult.dispatchUpdatesTo(this)
    }
}

