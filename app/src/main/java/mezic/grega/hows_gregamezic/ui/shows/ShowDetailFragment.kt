package mezic.grega.hows_gregamezic.ui.shows

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_show_detail.*
import kotlinx.android.synthetic.main.view_no_episodes.*
import kotlinx.android.synthetic.main.view_toolbar.*
import mezic.grega.hows_gregamezic.MainFragmentActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.ShowRepository
import mezic.grega.hows_gregamezic.database.models.ShowDetailModelDb
import mezic.grega.hows_gregamezic.ui.episodes.dummy.EpisodeAdapter
import mezic.grega.hows_gregamezic.network.Episodes
import mezic.grega.hows_gregamezic.network.Show
import mezic.grega.hows_gregamezic.network.ShowDetail
import mezic.grega.hows_gregamezic.network.SingletonApi
import mezic.grega.hows_gregamezic.utils.Util
import mezic.grega.hows_gregamezic.viewmodels.ShowDetailViewModel
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ShowDetailFragment : Fragment() {

    // Instance
    companion object {
        fun newIntent(_id: String): ShowDetailFragment {
            val args = Bundle()
            args.putString(Util.SHOW_ID_KEY, _id)
            val fragment = ShowDetailFragment()
            fragment.arguments = args
            return fragment
        }

        lateinit var showDetailCallback: ShowDetailCallback
        lateinit var viewModel: ShowDetailViewModel
        private var isNoError = false
    }

    private lateinit var showId: String
    private lateinit var showName: String
    private lateinit var showDescription: String


    override fun onAttach(context: Context) {
        super.onAttach(context)

        viewModel = ViewModelProviders.of(this).get(ShowDetailViewModel::class.java)

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

        progressbar.visibility = View.VISIBLE

        val showNullableId = arguments?.getString(Util.SHOW_ID_KEY)
        if (showNullableId != null)
            showId = showNullableId


        // get show details
        viewModel.getShowDetail(showId)
        viewModel.showDetailItem.observe(this, Observer { showDetail ->
            if (showDetail != null) {

                showName = showDetail.title
                showDescription = showDetail.description

                // add description text
                show_detail_description.text = showDescription

                // set toolbar title
                my_toolbar.title = showName
            } else {
                isNoError = false
            }
        })

        // get episode list
        viewModel.getEpisodeList(showId)
        viewModel.episodeList.observe(this, Observer { episodes ->
            progressbar.visibility = View.GONE
            if (episodes != null) {
                isNoError = true
                if (episodes.isNotEmpty()) {
                    linear_view_no_episodes.visibility = View.GONE
                    episodes_recycle_view.visibility = View.VISIBLE

                    // attach the adapter
                    (episodes_recycle_view as RecyclerView).layoutManager =
                        LinearLayoutManager(context)
                    (episodes_recycle_view as RecyclerView).adapter =
                        EpisodeAdapter(episodes) {
                            toast(it.title)
                        }
                } else {
                    linear_view_no_episodes.visibility = View.VISIBLE
                    episodes_recycle_view.visibility = View.GONE
                }
            } else {
                viewModel.error.observe(this, Observer {
                    toast(it.errorMessage)
                    isNoError = false
                })
            }
        })

        // enable navigation anyway
        my_toolbar.setNavigationOnClickListener {
            (context as MainFragmentActivity).supportFragmentManager.popBackStack()
        }

        // add episode listeners
        setAddEpisodeListeners()
    }

    private fun setAddEpisodeListeners() {
            tv_add_episodes.setOnClickListener {
                if (isNoError)
                    showDetailCallback.onAddEpisode(showId, showName, showDescription)
                else
                    toast("Please connect to the internet")
            }
            fab_add_episode.setOnClickListener {
                if (isNoError)
                    showDetailCallback.onAddEpisode(showId, showName, showDescription)
                else
                    toast("Please connect to the internet")
            }
    }
}

interface ShowDetailCallback {
    fun onAddEpisode(showId: String, showName: String?, showDescription: String?)
}