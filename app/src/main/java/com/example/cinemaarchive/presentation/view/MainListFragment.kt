package com.example.cinemaarchive.presentation.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cinemaarchive.R
import com.example.cinemaarchive.presentation.view.detailfilm.OnFilmDetailFragmentListener
import com.example.cinemaarchive.data.entity.Film
import com.example.cinemaarchive.presentation.recycler.FilmRecyclerAdapter
import com.example.cinemaarchive.presentation.viewModel.LoadingStates
import com.example.cinemaarchive.presentation.viewModel.MainListViewModel
import com.example.cinemaarchive.presentation.utils.PaginationScrollListener
import kotlinx.android.synthetic.main.main_fragment.*


class MainListFragment : Fragment() {

    private lateinit var filmRecyclerAdapter: FilmRecyclerAdapter
    private lateinit var mCallback: OnFilmDetailFragmentListener
    private val viewModel: MainListViewModel by lazy {
        ViewModelProvider(this).get(MainListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true
        initRecycler(ArrayList())

        mainSwiperefresh.setOnRefreshListener {
            main_progress.visibility = View.VISIBLE
            viewModel.refreshFistPage()
        }

        viewModel.responseLiveData.observe(viewLifecycleOwner, Observer {
            main_progress.visibility = View.GONE
            mainSwiperefresh.isRefreshing = false
            filmRecyclerAdapter.updateList(it)
        })

        viewModel.loadingStateLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoadingStates.LOADING -> showLoadingFooter()
                LoadingStates.LOADED -> hideLoadingFooter()
                LoadingStates.ERROR -> showError()
                else ->  Log.i("LoadingStates null","LoadingStates arguments is null")
            }
        })
    }

    private fun showError() {
        TODO("not implemented")
    }

    private fun showLoadingFooter(){
        filmRecyclerAdapter.addLoadingFooter()
    }

    private fun hideLoadingFooter(){
        filmRecyclerAdapter.removeLoadingFooter()
    }

    private fun initRecycler(items: List<Film>) {
        filmRecyclerAdapter =
            FilmRecyclerAdapter(
                items,
                { mCallback.onOpenDetailFragment(it) },
                { film: Film, position: Int, isFavoriteChecked: Boolean ->
                    viewModel.updateFavoriteList(film, isFavoriteChecked) }
            )
        mainFragmentRecycler.adapter = filmRecyclerAdapter
        setRecyclerScrollListener()
    }

    private fun setRecyclerScrollListener(){
        mainFragmentRecycler.addOnScrollListener(object :
            PaginationScrollListener(mainFragmentRecycler.layoutManager!!) {
            override fun lastItemReached() {
                viewModel.lastItemReached()
            }
            override fun isLoading(): Boolean {
                return when (viewModel.loadingStateLiveData.value) {
                    LoadingStates.LOADING -> true
                    else -> false
                }
            }
        })
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

