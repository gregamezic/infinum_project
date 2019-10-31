package mezic.grega.hows_gregamezic.shows

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_show_detail.*
import kotlinx.android.synthetic.main.view_no_episodes.*
import kotlinx.android.synthetic.main.view_toolbar.*
import mezic.grega.hows_gregamezic.MainFragmentActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.episodes.dummy.EpisodeAdapter
import mezic.grega.hows_gregamezic.shows.dummy.Show
import mezic.grega.hows_gregamezic.utils.Util
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

class ShowDetailFragment: Fragment() {

    // Instance
    companion object {
        fun newIntent(name: String?, description: String?): ShowDetailFragment {
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
    private var showName: String? = ""
    private var showDescription: String? = ""


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ShowDetailCallback) {
            showDetailCallback = context
        } else {
            throw RuntimeException("Please implement ShowCallback")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showName = arguments?.getString(Util.SHOW_NAME_KEY, "")
        showDescription = arguments?.getString(Util.SHOW_DESCRIPTION_KEY, "")

        // get id of the show
        showId = ShowFragment.shows.indexOfFirst { it.name == showName }

        // add description text
        show_detail_description.text = showDescription

        // set toolbar title
        my_toolbar.title = showName
        my_toolbar.setNavigationOnClickListener {
            (context as MainFragmentActivity).supportFragmentManager.popBackStack()
        }

        // add episode listeners
        setAddEpisodeListeners()

        // attach the adapter
        (episodes_recycle_view as RecyclerView).layoutManager = LinearLayoutManager(context)
        (episodes_recycle_view as RecyclerView).adapter = adapter

        //set screen
        if (ShowFragment.shows[showId].episodes.isEmpty())
            setEmptyScreen()
        else
            setEpisodesScreen()
    }

    private fun setEpisodesScreen() {
        linear_view_no_episodes.visibility = View.GONE
        episodes_recycle_view.visibility = View.VISIBLE

        adapter = EpisodeAdapter(ShowFragment.shows[showId].episodes) {
            toast(it.name)
        }
        (episodes_recycle_view as RecyclerView).adapter = adapter

    }

    private fun setEmptyScreen() {
        linear_view_no_episodes.visibility = View.VISIBLE
        episodes_recycle_view.visibility = View.GONE
    }


    private fun setAddEpisodeListeners() {
        tv_add_episodes.setOnClickListener {
            showDetailCallback?.onAddEpisode(showId, showName, showDescription)
        }
        fab_add_episode.setOnClickListener {
            showDetailCallback?.onAddEpisode(showId, showName, showDescription)
        }
    }
}

interface ShowDetailCallback {
    fun onAddEpisode(showId: Int, showName: String?, showDescription: String?)
}