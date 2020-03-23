package com.example.cinemaarchive.presentation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.cinemaarchive.R
import com.example.cinemaarchive.presentation.view.detailfilm.DetailFragment
import com.example.cinemaarchive.presentation.view.detailfilm.FILM_DETAIL_FRAGMENT_TAG
import com.example.cinemaarchive.presentation.view.detailfilm.IBottomNavOwner
import com.example.cinemaarchive.presentation.view.detailfilm.OnFilmDetailFragmentListener
import com.example.cinemaarchive.presentation.view.FAVORITE_LIST_FRAGMENT_TAG
import com.example.cinemaarchive.presentation.view.FavoriteListFragment
import com.example.cinemaarchive.presentation.view.FILM_LIST_FRAGMENT_TAG
import com.example.cinemaarchive.presentation.view.FilmListFragment
import com.example.cinemaarchive.data.entity.Film
import com.example.cinemaarchive.data.database.Database
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainActivity : AppCompatActivity(),
    OnFilmDetailFragmentListener,
    IBottomNavOwner{

    private val filmListFragment: FilmListFragment =
        FilmListFragment()
    private val favoriteListFragment: FavoriteListFragment =
        FavoriteListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportFragmentManager.findFragmentById(R.id.containerFragment) == null)
            addMainListFilmFragment()

        initBottomNavigation()

        initToolbar()

    }

    private fun addMainListFilmFragment() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, filmListFragment,
                FILM_LIST_FRAGMENT_TAG
            )
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    private fun initBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.bottom_navigation_favorite_menu -> {
                    showFavoriteFilmsList(Database.favoriteList)
                    true
                }
                R.id.bottom_navigation_home_menu -> {
                    showMainListFilmFragment()
                    true
                }
                else -> false
            }
        }
    }

    private fun initToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOpenDetailFragment(film: Film) {
        showDetailFilmFragment(film)
    }

    private fun showDetailFilmFragment(film: Film) {

        val bundle = Bundle()
        bundle.putParcelable("filmDetail", film)

        val filmDetailFragment =
            DetailFragment()
        filmDetailFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.containerFragment, filmDetailFragment,
                FILM_DETAIL_FRAGMENT_TAG
            )
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    private fun showFavoriteFilmsList(items: ArrayList<Film>) {
        if (supportFragmentManager.findFragmentByTag(FAVORITE_LIST_FRAGMENT_TAG) == null) {
            val bundle = Bundle()
            bundle.putParcelableArrayList("favoriteList", items)


            favoriteListFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.containerFragment, favoriteListFragment,
                    FAVORITE_LIST_FRAGMENT_TAG
                )
                .commit()
        }
    }

    private fun showMainListFilmFragment() {
        if (supportFragmentManager.findFragmentByTag(FILM_LIST_FRAGMENT_TAG) == null) {

            supportFragmentManager.beginTransaction()
                .replace(R.id.containerFragment, filmListFragment,
                    FILM_LIST_FRAGMENT_TAG
                )
                .commit()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.ic_share)
            share()
        return true
    }

    private fun share() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "CinemaArchive")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, getString(R.string.Invite_friends))
        startActivity(shareIntent)
    }

    override fun getBottomBar(): View {
        return bottomNavigationView
    }
}