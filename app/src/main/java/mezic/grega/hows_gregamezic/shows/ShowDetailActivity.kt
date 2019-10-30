package mezic.grega.hows_gregamezic.shows

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_show_detail.*
import kotlinx.android.synthetic.main.view_no_episodes.*
import mezic.grega.hows_gregamezic.MainBaseActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.utils.Util
import mezic.grega.hows_gregamezic.episodes.AddEpisodeActivity
import mezic.grega.hows_gregamezic.episodes.dummy.Episode
import mezic.grega.hows_gregamezic.episodes.dummy.EpisodeAdapter

class ShowDetailActivity : MainBaseActivity() {

    private var adapter: EpisodeAdapter = EpisodeAdapter(mutableListOf()) {
        toast(it.name)
    }
    private var showId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)

        val name = intent.getStringExtra(Util.SHOW_NAME_KEY)
        val description = intent.getStringExtra(Util.SHOW_DESCRIPTION_KEY)

        // get id of the show
        showId = ShowActivity.shows.indexOfFirst { it.name == name }

        // add description text
        show_detail_description.text = description

        // set toolbar title
        setupToolbar(name)

        // add episode listeners
        setAddEpisodeListeners()

        // attach the adapter
        (episodes_recycle_view as RecyclerView).layoutManager = LinearLayoutManager(this)
        (episodes_recycle_view as RecyclerView).adapter = adapter

        //set screen
        if (ShowActivity.shows[showId].episodes.isEmpty())
            setEmptyScreen()
        else
            setEpisodesScreen()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
    }

    private fun setAddEpisodeListeners() {
        tv_add_episodes.setOnClickListener {
            addEpisode()
        }
        fab_add_episode.setOnClickListener {
            addEpisode()
        }
    }

    private fun addEpisode() {
        val intent = Intent(this, AddEpisodeActivity::class.java)
        startActivityForResult(intent, Util.EPISODE_ADD_EPISODE_REQUEST_CODE)
    }

    private fun setEpisodesScreen() {
        linear_view_no_episodes.visibility = View.GONE
        episodes_recycle_view.visibility = View.VISIBLE

        adapter = EpisodeAdapter(ShowActivity.shows[showId].episodes) {
            toast(it.name)
        }
        (episodes_recycle_view as RecyclerView).adapter = adapter

    }

    private fun updateEpisodeList(episode: Episode) {
        linear_view_no_episodes.visibility = View.GONE
        episodes_recycle_view.visibility = View.VISIBLE

        adapter.addEpisode(episode)

    }

    private fun setEmptyScreen() {
        linear_view_no_episodes.visibility = View.VISIBLE
        episodes_recycle_view.visibility = View.GONE
    }
}
