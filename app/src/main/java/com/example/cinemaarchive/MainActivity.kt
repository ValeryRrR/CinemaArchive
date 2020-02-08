package com.example.cinemaarchive

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.otushomework1.Film
import kotlinx.android.synthetic.main.activity_main.*


const val DETAIL_REQUEST_CODE = 1
const val INTENT_EXTRA_NAME = "INTENT_EXTRA_NAME"

class MainActivity : AppCompatActivity() {

    lateinit var film: Film
    lateinit var film2: Film

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        film = Film(getString(R.string.spies_in_disgues), "desc", R.drawable.spies_in_disgues, false, "")
        film2 = Film(getString(R.string.union_of_safety), "desc", R.drawable.union_of_safety, false, "")

        onRestoreState(savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        button_detail.setOnClickListener {
            startDetailActivity(film, INTENT_EXTRA_NAME)
        }
        button_detail_2.setOnClickListener {
            startDetailActivity(film2, INTENT_EXTRA_NAME)
        }
    }

    private fun startDetailActivity(film: Film, intentExtraName: String) {
        startActivityForResult((Intent(this, DetailActivity::class.java).putExtra(intentExtraName, film)), DETAIL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (intent == null) return

        if (requestCode == DETAIL_REQUEST_CODE) {
            when ((intent.getParcelableExtra(INTENT_EXTRA_NAME) as Film).name) {
                "Spies in disgues" -> { film = intent.getParcelableExtra(INTENT_EXTRA_NAME); logResultFilm(film) }
                "Union of safety" -> { film2 = intent.getParcelableExtra(INTENT_EXTRA_NAME); logResultFilm(film2) }
            }
        }
    }

    private fun logResultFilm(film: Film) {
        Log.d("Result", film.name + " comment = " + film.comment + ",  film favorite = " + film.isFavorite)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putParcelable("Film1", film)
            putParcelable("Film2", film2)
        }
    }

    private fun onRestoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            film = savedInstanceState.getParcelable("Film1") as Film
            film2 = savedInstanceState.getParcelable("Film2") as Film
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.ic_share -> share()
            R.id.dark_mode -> changeTheme()
        }
        return true
    }

    private fun share() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, film.name + "\n" + film.comment)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, getString(R.string.Invite_friends))
        startActivity(shareIntent)
    }

     override fun  onBackPressed() {
        showCloseDialog()
    }

    private fun showCloseDialog(){
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage(getString(R.string.closeDialogMessage))
                .setTitle(getString(R.string.closeDialogTitle))
            setPositiveButton(R.string.ok) { dialog, id -> super.onBackPressed() }
            setNegativeButton(R.string.cancel) { dialog, id -> }
            create().show()
        }
    }

    private fun changeTheme(){
        val currentNightMode = (resources.configuration.uiMode
                and Configuration.UI_MODE_NIGHT_MASK)
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)}
            Configuration.UI_MODE_NIGHT_NO -> { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> { Log.d("", "UI_MODE_NIGHT_UNDEFINED")}
        }
    }
}
