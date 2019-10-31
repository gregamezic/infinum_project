package mezic.grega.hows_gregamezic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import mezic.grega.hows_gregamezic.episodes.AddEpisodeActivity
import mezic.grega.hows_gregamezic.episodes.dummy.Episode
import mezic.grega.hows_gregamezic.login.LoginActivity
import mezic.grega.hows_gregamezic.shows.*
import mezic.grega.hows_gregamezic.utils.Util
import org.jetbrains.anko.toast

class MainFragmentActivity : MainBaseActivity(), ShowCallback/*, ShowDetailCallback*/ {

    private var showId: Int = -1

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
            .add(R.id.fragment_container, ShowDetailFragment.newIntent(name, description, year))
            .addToBackStack(ShowDetailFragment::class.java.name)
            .commit()
    }


    /**
     * Show detail fragment functions
     */
    /*override fun onAddEpisode(showId: Int) {
        this.showId = showId
        val intent = Intent(this, AddEpisodeActivity::class.java)
        startActivityForResult(intent, Util.EPISODE_ADD_EPISODE_REQUEST_CODE)
    }*/

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Util.EPISODE_ADD_EPISODE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val episodeName = data.getStringExtra(Util.EPISODE_NAME)
                val episodeDescription = data.getStringExtra(Util.EPISODE_DESCRIPTION)
                val imgPath = data.getStringExtra(Util.EPISODE_IMAGE_PATH_KEY)
                val numberName = "${ShowActivity.shows[showId].episodes.size + 1}. $episodeName"

                val episode = Episode(numberName, episodeDescription, imgPath)

                ShowActivity.shows[showId].episodes.add(episode)

                when {
                    ShowActivity.shows[showId].episodes.isEmpty() -> setEmptyScreen()
                    else -> updateEpisodeList(episode)
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                toast("Canceled adding episode")
            }
        }
    }*/
}