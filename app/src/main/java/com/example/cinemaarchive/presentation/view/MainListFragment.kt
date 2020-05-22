package com.example.cinemaarchive.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cinemaarchive.App
import com.example.cinemaarchive.R
import com.example.cinemaarchive.presentation.view.detailfilm.OnFilmDetailFragmentListener
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.presentation.enam.LoadingStates
import com.example.cinemaarchive.presentation.recycler.FilmRecyclerAdapter
import com.example.cinemaarchive.presentation.viewModel.MainListViewModel
import com.example.cinemaarchive.presentation.utils.PaginationScrollListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.no_internet_connection.*


class MainListFragment : Fragment() {

    private lateinit var filmRecyclerAdapter: FilmRecyclerAdapter
    private lateinit var mCallback: OnFilmDetailFragmentListener
    private val viewModel: MainListViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            mainViewModelFactory
        ).get(MainListViewModel::class.java)
    }
    private var snackBar: Snackbar? = null

    private val mainViewModelFactory = App.instance!!.mainViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler(ArrayList())
        noInternetConnection.visibility = View.GONE

        mainSwiperefresh.setOnRefreshListener {
            main_progress.visibility = View.VISIBLE
            snackBar?.dismiss()
            viewModel.refreshFistPage()
        }

        retry.setOnClickListener { viewModel.refreshFistPage() }

        viewModel.responseLiveData.observe(viewLifecycleOwner, Observer {
            hideProgresses()
            filmRecyclerAdapter.updateList(it)
        })

        viewModel.errorLoadingNextPageLiveData.observe(viewLifecycleOwner, Observer {
            val error = it.getContentIfNotHandled()
            if (error != null)
                showErrorSnackBar(error)
        })

        viewModel.loadingStateLiveData.observe(viewLifecycleOwner, Observer { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let {
                when (it) {
                    LoadingStates.LOADING -> showLoadingFooter()
                    LoadingStates.LOADED -> hideLoadingFooter()
                    LoadingStates.ERROR -> showError()
                }
            }
        })
    }

    private fun showError() {
        main_progress.visibility = View.GONE
        massage.text = viewModel.errorLiveData.value?.peekContent()
        noInternetConnection.visibility = View.VISIBLE
    }

    private fun showLoadingFooter() {
        filmRecyclerAdapter.addLoadingFooter()
    }

    private fun hideLoadingFooter() {
        filmRecyclerAdapter.removeLoadingFooter()
    }

    private fun hideProgresses() {
        noInternetConnection.visibility = View.GONE
        main_progress.visibility = View.GONE
        mainSwiperefresh.isRefreshing = false
        snackBar?.dismiss()
    }

    private fun showErrorSnackBar(contentIfNotHandled: String) {
        if (snackBar?.isShown == false || snackBar == null) {
            snackBar = Snackbar
                .make(
                    requireView(),
                    contentIfNotHandled,
                    Snackbar.LENGTH_INDEFINITE
                )

            snackBar?.setAction(getString(R.string.repeat)) {
                viewModel.lastItemReached()
                snackBar = null
            }
            snackBar?.show()
        }
    }

    private fun initRecycler(items: List<Film>) {
        filmRecyclerAdapter =
            FilmRecyclerAdapter(
                items,
                { mCallback.onOpenDetailFragment(it) },
                { film: Film, position: Int, isFavoriteChecked: Boolean ->
                    viewModel.onLikeBtnClicked(film, position, isFavoriteChecked)
                }
            )
        mainFragmentRecycler.adapter = filmRecyclerAdapter
        setRecyclerScrollListener()
    }

    private fun setRecyclerScrollListener() {
        mainFragmentRecycler.addOnScrollListener(object :
            PaginationScrollListener(mainFragmentRecycler.layoutManager!!) {
            override fun lastItemReached() {
                viewModel.lastItemReached()
            }

            override fun isLoading(): Boolean {
                return when (viewModel.loadingStateLiveData.value?.peekContent()) {
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            filmRecyclerAdapter.notifyDataSetChanged()
            snackBar?.view?.visibility = View.VISIBLE
        } else if (hidden) {
            snackBar?.view?.visibility = View.INVISIBLE
        }
    }
}

