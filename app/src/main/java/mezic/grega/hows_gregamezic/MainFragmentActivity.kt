package mezic.grega.hows_gregamezic

import android.content.Intent
import android.os.Bundle
import mezic.grega.hows_gregamezic.episodes.AddEpisodeCallback
import mezic.grega.hows_gregamezic.episodes.AddEpisodeFragment
import mezic.grega.hows_gregamezic.episodes.dummy.Episode
import mezic.grega.hows_gregamezic.login.LoginActivity
import mezic.grega.hows_gregamezic.shows.*

class MainFragmentActivity : MainBaseActivity(), ShowCallback, ShowDetailCallback, AddEpisodeCallback {

    private var showId: Int = -1
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

    override fun onShowItemClick(name: String, description: String, year: String) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ShowDetailFragment.newIntent(name, description))
            .addToBackStack(ShowDetailFragment::class.java.name)
            .commit()
    }

    override fun onAddEpisode(showId: Int, showName: String?, showDescription: String?) {
        this.showId = showId
        this.showName = showName
        this.showDescription = showDescription

        //supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, AddEpisodeFragment.newIntent(showId))
            .addToBackStack(AddEpisodeFragment::class.java.name)
            .commit()
    }

    override fun onEpisodeSave(name: String, description: String, imgPath: String?, showId: Int) {

        val numberName = "${ShowFragment.shows[showId].episodes.size + 1}. $name"
        val episode = Episode(numberName, description, imgPath)
        ShowFragment.shows[showId].episodes.add(episode)

        supportFragmentManager.popBackStack()
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ShowDetailFragment.newIntent(showName, showDescription))
            .addToBackStack(ShowDetailFragment::class.java.name)
            .commit()
    }
}