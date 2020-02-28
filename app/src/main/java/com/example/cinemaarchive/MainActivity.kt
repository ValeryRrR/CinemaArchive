package com.example.cinemaarchive

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.cinemaarchive.detailfilm.DetailFragment
import com.example.cinemaarchive.detailfilm.OnFilmDetailFragmentListener
import com.example.cinemaarchive.faforitelist.FavoriteListFragment
import com.example.cinemaarchive.mainfilmlist.FilmListFragment
import com.example.cinemaarchive.repository.Film
import com.example.cinemaarchive.repository.database.Database
import kotlinx.android.synthetic.main.activity_main.*

import com.example.cinemaarchive.detailfilm.FILM_DETAIL_FRAGMENT_TAG
import com.example.cinemaarchive.detailfilm.IBottomNavOwner
import com.example.cinemaarchive.mainfilmlist.FILM_LIST_FRAGMENT_TAG
import com.example.cinemaarchive.faforitelist.FAVORITE_LIST_FRAGMENT_TAG
import kotlinx.android.synthetic.main.main_fragment.*

class MainActivity : AppCompatActivity(),
    OnFilmDetailFragmentListener,
    IBottomNavOwner{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportFragmentManager.findFragmentById(R.id.container_fragment) == null)
            addMainListFilmFragment(/*Database.items*/)

        initBottomNavigation()

        initToolbar()

    }

    private fun addMainListFilmFragment(/*items: ArrayList<Film>*/) {

        /*val bundle = Bundle()
        bundle.putParcelableArrayList("filmList", items)*/

        val filmListFragment = FilmListFragment()
        /*filmListFragment.arguments = bundle*/

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, filmListFragment, FILM_LIST_FRAGMENT_TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    private fun initBottomNavigation() {
        bottom_navigation_view.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.bottom_navigation_favorite_menu -> {
                    showFavoriteFilmsList(Database.favoriteList)
                    true
                }
                R.id.bottom_navigation_home_menu -> {
       //             showMainListFilmFragment(Database.items)
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

        val filmDetailFragment = DetailFragment()
        filmDetailFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, filmDetailFragment, FILM_DETAIL_FRAGMENT_TAG)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    private fun showFavoriteFilmsList(items: ArrayList<Film>) {
        if (supportFragmentManager.findFragmentByTag(FAVORITE_LIST_FRAGMENT_TAG) == null) {
            val bundle = Bundle()
            bundle.putParcelableArrayList("favoriteList", items)

            val fragobj = FavoriteListFragment()
            fragobj.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.container_fragment, fragobj, FAVORITE_LIST_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }

    private fun showMainListFilmFragment(items: ArrayList<Film>) {
        if (supportFragmentManager.findFragmentByTag(FILM_LIST_FRAGMENT_TAG) == null) {
            val bundle = Bundle()
            bundle.putParcelableArrayList("filmList", items)

            val filmListFragment = FilmListFragment()
            filmListFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.container_fragment, filmListFragment, FILM_LIST_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
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
        return bottom_navigation_view
    }
}