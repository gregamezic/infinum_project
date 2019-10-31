package mezic.grega.hows_gregamezic.shows

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_show_detail.*
import kotlinx.android.synthetic.main.view_no_episodes.*
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.episodes.AddEpisodeActivity
import mezic.grega.hows_gregamezic.episodes.dummy.Episode
import mezic.grega.hows_gregamezic.episodes.dummy.EpisodeAdapter
import mezic.grega.hows_gregamezic.utils.ToolbarHelper
import mezic.grega.hows_gregamezic.utils.Util
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

class ShowDetailFragment: Fragment() {

    // Instance
    companion object {
        fun newIntent(name: String, description: String, year: String?): ShowDetailFragment {
            val args = Bundle()
            args.putString(Util.SHOW_NAME_KEY, name)
            args.putString(Util.SHOW_DESCRIPTION_KEY, description)
            val fragment = ShowDetailFragment()
            fragment.arguments = args
            return fragment
        }

        var showDetailCallback: ShowDetailCallback? = null
    }

    // vars
    private var adapter: EpisodeAdapter = EpisodeAdapter(mutableListOf()) {
        context?.toast(it.name)
    }
    private var showId: Int = -1


    // functions
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString(Util.SHOW_NAME_KEY, "")
        val description = arguments?.getString(Util.SHOW_DESCRIPTION_KEY, "")

        // get id of the show
        showId = ShowActivity.shows.indexOfFirst { it.name == name }

        // add description text
        show_detail_description.text = description

        // set toolbar title
        context?.let { ToolbarHelper(it).setupToolbar(name) }

        // add episode listeners
        setAddEpisodeListeners()

        // attach the adapter
        (episodes_recycle_view as RecyclerView).layoutManager = LinearLayoutManager(context)
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
        val intent = Intent(context, AddEpisodeActivity::class.java)
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

    private fun setEmptyScreen() {
        linear_view_no_episodes.visibility = View.VISIBLE
        episodes_recycle_view.visibility = View.GONE
    }

    private fun updateEpisodeList(episode: Episode) {
        linear_view_no_episodes.visibility = View.GONE
        episodes_recycle_view.visibility = View.VISIBLE

        adapter.addEpisode(episode)
    }

    /*private fun setAddEpisodeListeners() {
        tv_add_episodes.setOnClickListener {
            showDetailCallback?.onAddEpisode(showId)
        }
        fab_add_episode.setOnClickListener {
            showDetailCallback?.onAddEpisode(showId)
        }
    }*/
}

interface ShowDetailCallback {
    fun onAddEpisode(showId: Int)
}