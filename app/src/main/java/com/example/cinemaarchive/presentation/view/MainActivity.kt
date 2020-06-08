package com.example.cinemaarchive.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.cinemaarchive.R
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.presentation.enam.BottomNavigationTabs
import com.example.cinemaarchive.presentation.navigation.Router
import com.example.cinemaarchive.presentation.utils.filmFromMap
import com.example.cinemaarchive.presentation.view.detail.IBottomNavOwner
import com.example.cinemaarchive.presentation.view.detail.OnFilmDetailFragmentListener
import com.example.cinemaarchive.presentation.view.remind.Notifier.Companion.REMIND_FILM
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_fragment.*


class MainActivity : AppCompatActivity(),
    OnFilmDetailFragmentListener,
    IBottomNavOwner {

    private val router: Router = Router(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNavigation()

        initToolbar()

        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.bottom_navigation_home_menu
        }

        checkFirebaseNotification()

        checkReminder()

        logFirebaseToken()
    }

    private fun initBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.bottom_navigation_favorite_menu -> {
                    router.selectTab(BottomNavigationTabs.FILM_FAVORITE)
                    true
                }
                R.id.bottom_navigation_home_menu -> {
                    router.selectTab(BottomNavigationTabs.FILM_LIST)
                    true
                }
                else -> false
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOpenDetailFragment(film: Film) {
        router.showDetailFilmFragment(film)
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

    private fun checkFirebaseNotification() {
        if (intent?.extras?.get("flag_fcm") != null) {
            val extrasMap = mutableMapOf<String, String>()
            val bundle: Bundle = intent?.extras as Bundle

            for (key in bundle.keySet()) {
                val value = bundle[key]?.toString()
                    ?: throw NullPointerException("Value for the key is null!")
                extrasMap[key] = value
            }
            router.showDetailFilmFragment(filmFromMap(extrasMap))
        }
    }

    private fun checkReminder() {
        val remindFilm: Film? = intent.getParcelableExtra(REMIND_FILM)
        if (remindFilm != null) {
            router.showDetailFilmFragment(remindFilm)
        }
    }

    private fun logFirebaseToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Token", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result?.token
                Log.d("Token", token)
            })
    }
}