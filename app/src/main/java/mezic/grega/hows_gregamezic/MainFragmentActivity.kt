package mezic.grega.hows_gregamezic

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import mezic.grega.hows_gregamezic.ui.episodes.AddEpisodeCallback
import mezic.grega.hows_gregamezic.ui.episodes.AddEpisodeFragment
import mezic.grega.hows_gregamezic.ui.episodes.EpisodesDetailsFragment
import mezic.grega.hows_gregamezic.ui.login.LoginActivity
import mezic.grega.hows_gregamezic.ui.shows.*
import mezic.grega.hows_gregamezic.utils.FragmentBackPressed
import mezic.grega.hows_gregamezic.viewmodels.ShowViewModel


class MainFragmentActivity : MainBaseActivity(), ShowCallback, ShowDetailCallback, AddEpisodeCallback {

    private var showId: String = ""
    private var showName: String? = ""
    private var showDescription: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fragment)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ShowFragment.newIntent())
            .commit()
    }


    /**
     * Show fragment functions
     */
    override fun onLogout() {
        mSharedPreferencesManager.setUserLogin(false)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onShowItemClick(_id: String) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ShowDetailFragment.newIntent(_id))
            .addToBackStack(ShowDetailFragment::class.java.name)
            .commit()
    }

    override fun onAddEpisode(showId: String, showName: String?, showDescription: String?) {
        this.showId = showId
        this.showName = showName
        this.showDescription = showDescription

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, AddEpisodeFragment.newIntent(this.showId))
            .addToBackStack(AddEpisodeFragment::class.java.name)
            .commit()
    }

    override fun onEpisodeSave(showId: String) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ShowDetailFragment.newIntent(showId))
            .addToBackStack(ShowDetailFragment::class.java.name)
            .commit()
    }

    override fun onEpisodeClick(episodeId: String) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, EpisodesDetailsFragment.newIntent(episodeId))
            .addToBackStack(EpisodesDetailsFragment::class.java.name)
            .commit()
    }

    override fun noToken() {
        startActivity(Intent(this@MainFragmentActivity, LoginActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment != null && fragment is FragmentBackPressed) {
            if (!fragment.onBackPressed()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
}