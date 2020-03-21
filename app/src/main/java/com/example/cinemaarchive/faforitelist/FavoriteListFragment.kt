package com.example.cinemaarchive.faforitelist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cinemaarchive.R
import com.example.cinemaarchive.detailfilm.OnFilmDetailFragmentListener
import com.example.cinemaarchive.repository.Film
import com.example.cinemaarchive.repository.FilmRecyclerAdapter
import com.example.cinemaarchive.repository.database.Database
import kotlinx.android.synthetic.main.activity_main.*
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
            favorites_list_empty.text = getString(R.string.favorites_list_empty)
        else showRecyclerWithFilms(favoriteList)
    }


    private fun showRecyclerWithFilms(items: List<Film>) {

        favorite_list_recycler.adapter =
            FilmRecyclerAdapter(
                LayoutInflater.from(favorite_list_recycler.context),
                items,
                object :
                    FilmRecyclerAdapter.OnItemClickListener {
                    override fun onItemClicked(film: Film) {
                        mCallback?.onOpenDetailFragment(film)
                    }
                },
                object :
                    FilmRecyclerAdapter.OnLikeClickListener {
                    override fun onLikeClicked(film: Film, position: Int, isFavoriteChecked: Boolean) {
                        film.isFavorite = isFavoriteChecked
                        if (!isFavoriteChecked) {
                            Database.favoriteList.remove(film)
                            favorite_list_recycler.adapter?.notifyItemRemoved(position)
                            (favorite_list_recycler.adapter as FilmRecyclerAdapter).onItemRemove(position,film, favorite_list_recycler)
                        }
                    }
                },
                context!!)
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

