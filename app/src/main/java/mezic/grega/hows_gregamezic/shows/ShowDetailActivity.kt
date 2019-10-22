package mezic.grega.hows_gregamezic.shows

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_show_detail.*
import kotlinx.android.synthetic.main.view_no_episodes.*
import kotlinx.android.synthetic.main.view_toolbar.*
import mezic.grega.hows_gregamezic.MainBaseActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.Util
import mezic.grega.hows_gregamezic.episodes.AddEpisodeActivity
import mezic.grega.hows_gregamezic.episodes.dummy.Episode
import mezic.grega.hows_gregamezic.episodes.dummy.EpisodeAdapter
import mezic.grega.hows_gregamezic.shows.dummy.Show

class ShowDetailActivity: MainBaseActivity() {

    private var adapter: EpisodeAdapter? = null
    private var showId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)

        val name = intent.getStringExtra(Util().SHOW_NAME_KEY)
        val description = intent.getStringExtra(Util().SHOW_DESCRIPTION_KEY)
        //val year = intent.getStringExtra(Util().SHOW_YEAR_KEY)


        for (i in 0..ShowActivity.shows.size) {
            if (ShowActivity.shows[i].name == name) {
                showId = i
                break
            }
        }

        // add description text
        show_detail_description.text = description

        // set toolbar title
        setupToolbar()
        setToolbarName(name)
        my_toolbar.setNavigationOnClickListener { finish() }

        // add episode listeners
        setAddEpisodeListeners()

        //set screen
        if(ShowActivity.shows[showId].episodes.isEmpty())
            setEmptyScreen()
        else
            setEpisodesScreen(ShowActivity.shows[showId].episodes)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Util().EPISODE_ADD_EPISODE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val episodeName = data?.getStringExtra(Util().EPISODE_NAME)
                val episodeDescription = data?.getStringExtra(Util().EPISODE_DESCRIPTION)

                val numberName = "${ShowActivity.shows[showId].episodes.size+1}. $episodeName"

                ShowActivity.shows[showId].episodes.add(Episode(numberName, episodeDescription!!))

                // check for first item
                when {
                    ShowActivity.shows[showId].episodes.size == 1 -> setEpisodesScreen(ShowActivity.shows[showId].episodes)
                    ShowActivity.shows[showId].episodes.isEmpty() -> setEmptyScreen()
                    else -> updateEpisodeList(ShowActivity.shows[showId].episodes)
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
        startActivityForResult(intent, Util().EPISODE_ADD_EPISODE_REQUEST_CODE)
    }

    private fun setEpisodesScreen(episodes: List<Episode>) {
        linear_view_no_episodes.visibility = View.GONE
        episodes_recycle_view.visibility = View.VISIBLE

        // create adapter
        adapter = EpisodeAdapter(episodes) {
            toast(it.name)
        }
        //set view and adapter
        (episodes_recycle_view as RecyclerView).layoutManager = LinearLayoutManager(this)
        (episodes_recycle_view as RecyclerView).adapter = adapter
    }

    private fun updateEpisodeList(episodes: List<Episode>) {
        adapter!!.notifyItemChanged(episodes.size)
    }

    private fun setEmptyScreen() {
        linear_view_no_episodes.visibility = View.VISIBLE
        episodes_recycle_view.visibility = View.GONE
    }
}
