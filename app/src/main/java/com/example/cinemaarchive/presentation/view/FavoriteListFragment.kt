package com.example.cinemaarchive.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cinemaarchive.R
import com.example.cinemaarchive.presentation.view.detailfilm.OnFilmDetailFragmentListener
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.presentation.recycler.FilmRecyclerAdapter
import com.example.cinemaarchive.presentation.viewModel.FavoriteListViewModel
import kotlinx.android.synthetic.main.favarite_list_fragment.*


class FavoriteListFragment : Fragment() {

    private val viewModel: FavoriteListViewModel by lazy {
        ViewModelProvider(this).get(FavoriteListViewModel::class.java)
    }
    private var mCallback: OnFilmDetailFragmentListener? = null
    private lateinit var filmRecyclerAdapter: FilmRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favarite_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler(ArrayList())

        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            favoritesListEmpty.text = it
        })

        viewModel.favoriteListLiveData.observe(viewLifecycleOwner, Observer {
            filmRecyclerAdapter.updateList(it)
        } )
    }

    private fun initRecycler(items: List<Film>) {
        filmRecyclerAdapter = FilmRecyclerAdapter(
                items,
                { mCallback?.onOpenDetailFragment(it) },
                { film: Film, position: Int, isFavoriteChecked: Boolean ->
                    run {
                        film.isFavorite = isFavoriteChecked
                        if (!isFavoriteChecked){ removeFilmFromFavorites(film, position) }
                    }
                }
            )
        favoriteListRecycler.adapter = filmRecyclerAdapter
    }

    private fun removeFilmFromFavorites(film: Film, position: Int){
        favoriteListRecycler.adapter?.notifyItemRemoved(position)
        (favoriteListRecycler.adapter as FilmRecyclerAdapter).onItemRemove(
            position,
            film,
            favoriteListRecycler
        )
        viewModel.removeFavoriteFilm(film)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
            viewModel.refreshFavoriteList()
            filmRecyclerAdapter.notifyDataSetChanged()
        }
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

