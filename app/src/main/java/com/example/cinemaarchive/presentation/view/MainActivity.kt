package com.example.cinemaarchive.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.cinemaarchive.R
import com.example.cinemaarchive.presentation.view.detailfilm.DetailFragment
import com.example.cinemaarchive.presentation.view.detailfilm.FILM_DETAIL_FRAGMENT_TAG
import com.example.cinemaarchive.presentation.view.detailfilm.IBottomNavOwner
import com.example.cinemaarchive.presentation.view.detailfilm.OnFilmDetailFragmentListener
import com.example.cinemaarchive.data.entity.Film
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainActivity : AppCompatActivity(),
    OnFilmDetailFragmentListener,
    IBottomNavOwner{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNavigation()

        initToolbar()

        if(savedInstanceState == null){
            bottomNavigationView.selectedItemId = R.id.bottom_navigation_home_menu
        }
    }

    private fun initBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            Log.i("select item", item.itemId.toString())
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.bottom_navigation_favorite_menu -> {
                    selectTab(BottomNavigationTabs.FILM_FAVORITE)
                    true
                }
                R.id.bottom_navigation_home_menu -> {
                    selectTab(BottomNavigationTabs.FILM_LIST)
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
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val filmDetailFragment = DetailFragment()

        bundle.putParcelable("filmDetail", film)
        filmDetailFragment.arguments = bundle
        hideAllFragmentsExcept(
            filmDetailFragment,
            supportFragmentManager,
            fragmentTransaction)

        fragmentTransaction
            .add(R.id.containerFragment, filmDetailFragment, FILM_DETAIL_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
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
            putExtra(Intent.EXTRA_TEXT, "CinemaArchiveTest")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(
            sendIntent,
            getString(R.string.Invite_friends)
        )
        startActivity(shareIntent)
    }

    override fun getBottomBar(): View {
        return bottomNavigationView
    }

    private fun selectTab(tab: BottomNavigationTabs){
        val fragmentFromFragmentManager =
            supportFragmentManager.findFragmentByTag(tab.tag)
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if (fragmentFromFragmentManager == null){
            val newFragment = createFragmentByTab(tab)
            fragmentTransaction
                .add(R.id.containerFragment, newFragment, tab.tag)
        }else{
            if (fragmentFromFragmentManager.isHidden) {
                fragmentTransaction.
                    show(fragmentFromFragmentManager)
            }
        }
        hideAllFragmentsExcept(
            fragmentFromFragmentManager,
            supportFragmentManager,
            fragmentTransaction)
        fragmentTransaction.commit()
    }

    private fun hideAllFragmentsExcept(
        exceptFragment: Fragment?,
        fromFragmentManager: FragmentManager,
        withFragmentTransaction: FragmentTransaction){
        fromFragmentManager.fragments.asSequence().filterNotNull().filter { it.isVisible }
            .forEach {
                if (exceptFragment != it) {
                    withFragmentTransaction.hide(it)
                }
            }
    }

    private fun createFragmentByTab(tab: BottomNavigationTabs): Fragment {
        return when (tab) {
            BottomNavigationTabs.FILM_LIST -> MainListFragment()
            BottomNavigationTabs.FILM_FAVORITE -> FavoriteListFragment()
        }
    }

    enum class BottomNavigationTabs(val tag: String){
        FILM_LIST("FILM_LIST_FRAGMENT"),
        FILM_FAVORITE("FILM_LIST_FAVORITE")
    }
}