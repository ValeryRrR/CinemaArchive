package com.example.cinemaarchive.presentation.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.cinemaarchive.R
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.presentation.enam.BottomNavigationTabs
import com.example.cinemaarchive.presentation.view.FavoriteListFragment
import com.example.cinemaarchive.presentation.view.MainListFragment
import com.example.cinemaarchive.presentation.view.detail.DetailFragment
import com.example.cinemaarchive.presentation.view.detail.FILM_DETAIL_FRAGMENT_TAG

class Router(private val supportFragmentManager: FragmentManager) {

    fun showDetailFilmFragment(film: Film) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val filmDetailFragment = DetailFragment().newInstance(film)

        hideAllFragmentsExcept(filmDetailFragment, supportFragmentManager, fragmentTransaction)

        fragmentTransaction
            .add(R.id.containerFragment, filmDetailFragment, FILM_DETAIL_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }

    fun selectTab(tab: BottomNavigationTabs){
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

    private fun createFragmentByTab(tab: BottomNavigationTabs): Fragment {
        return when (tab) {
            BottomNavigationTabs.FILM_LIST -> MainListFragment()
            BottomNavigationTabs.FILM_FAVORITE -> FavoriteListFragment()
        }
    }

    private fun hideAllFragmentsExcept(
        exceptFragment: Fragment?,
        fromFragmentManager: FragmentManager,
        withFragmentTransaction: FragmentTransaction
    ){
        fromFragmentManager.fragments.asSequence().filterNotNull().filter { it.isVisible }
            .forEach {
                if (exceptFragment != it) {
                    withFragmentTransaction.hide(it)
                }
            }
    }
}