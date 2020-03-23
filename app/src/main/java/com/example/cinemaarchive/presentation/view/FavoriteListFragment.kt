package com.example.cinemaarchive.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cinemaarchive.R
import com.example.cinemaarchive.presentation.view.detailfilm.OnFilmDetailFragmentListener
import com.example.cinemaarchive.data.entity.Film
import com.example.cinemaarchive.presentation.recycler.FilmRecyclerAdapter
import com.example.cinemaarchive.data.database.Database
import kotlinx.android.synthetic.main.favarite_list_fragment.*

const val FAVORITE_LIST_FRAGMENT_TAG = "FAVORITE_LIST_FRAGMENT"


class FavoriteListFragment : Fragment() {

    var mCallback: OnFilmDetailFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favarite_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteList: List<Film>? = arguments?.getParcelableArrayList("favoriteList")

        if (favoriteList == null || favoriteList.isEmpty())
            favoritesListEmpty.text = getString(R.string.favoritesListEmpty)
        else showRecyclerWithFilms(favoriteList)
    }

    private fun showRecyclerWithFilms(items: List<Film>) {

        favoriteListRecycler.adapter =
            FilmRecyclerAdapter(
                LayoutInflater.from(favoriteListRecycler.context),
                items,
                { mCallback?.onOpenDetailFragment(it) },
                { film: Film, position: Int, isFavoriteChecked: Boolean ->
                    run {
                        film.isFavorite = isFavoriteChecked
                        if (!isFavoriteChecked) {
                            Database.favoriteList.remove(film)
                            favoriteListRecycler.adapter?.notifyItemRemoved(position)
                            (favoriteListRecycler.adapter as FilmRecyclerAdapter).onItemRemove(
                                position,
                                film,
                                favoriteListRecycler
                            )
                        }
                    }
                },
                context!!
            )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFilmDetailFragmentListener) {
            mCallback = context
        } else {
            throw RuntimeException(
                "$context must implement OnFilmDetailFragmentListener"
            )
        }
    }
}

