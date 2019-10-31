package mezic.grega.hows_gregamezic

import android.content.Intent
import android.os.Bundle
import mezic.grega.hows_gregamezic.episodes.AddEpisodeCallback
import mezic.grega.hows_gregamezic.episodes.AddEpisodeFragment
import mezic.grega.hows_gregamezic.episodes.dummy.Episode
import mezic.grega.hows_gregamezic.login.LoginActivity
import mezic.grega.hows_gregamezic.shows.*
import mezic.grega.hows_gregamezic.shows.dummy.Show

class MainFragmentActivity : MainBaseActivity(), ShowCallback, ShowDetailCallback, AddEpisodeCallback {

    companion object {
        val shows = mutableListOf(
            Show(
                R.drawable.icon_tbbt,
                "The Big Bang Theory",
                "(2007 - 2019)",
                "A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory."
            ),
            Show(
                R.drawable.icon_to,
                "The Office",
                "(2005 - 2013)",
                "A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium."
            ),
            Show(
                R.drawable.icon_house,
                "House M.D.",
                "(2004 - 2012)",
                "An antisocial maverick doctor who specializes in diagnostic medicine does whatever it takes to solve puzzling cases that come his way using his crack team of doctors and his wits."
            ),
            Show(
                R.drawable.icon_jtv,
                "Jane the Virgin",
                "(2004 - )",
                "A young, devout Catholic woman discovers that she was accidentally artificially inseminated."
            ),
            Show(
                R.drawable.icon_sherlock,
                "Sherlock",
                "(2010 - )",
                "A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory."
            )
        )
    }

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

    override fun onEpisodeSave(name: String, description: String, season_episode: String, imgPath: String?, showId: Int) {

        val numberName = "${shows[showId].episodes.size + 1}. $name"
        val episode = Episode(numberName, description, season_episode, imgPath)
        shows[showId].episodes.add(episode)

        supportFragmentManager.popBackStack()
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ShowDetailFragment.newIntent(showName, showDescription))
            .addToBackStack(ShowDetailFragment::class.java.name)
            .commit()
    }
}