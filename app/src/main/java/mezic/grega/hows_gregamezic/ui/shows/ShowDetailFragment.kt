package mezic.grega.hows_gregamezic.shows

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_show_detail.*
import kotlinx.android.synthetic.main.view_no_episodes.*
import kotlinx.android.synthetic.main.view_toolbar.*
import mezic.grega.hows_gregamezic.MainFragmentActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.episodes.dummy.EpisodeAdapter
import mezic.grega.hows_gregamezic.network.Episodes
import mezic.grega.hows_gregamezic.network.Show
import mezic.grega.hows_gregamezic.network.ShowDetail
import mezic.grega.hows_gregamezic.network.SingletonApi
import mezic.grega.hows_gregamezic.utils.Util
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailFragment: Fragment() {

    // Instance
    companion object {
        fun newIntent(_id: String): ShowDetailFragment {
            val args = Bundle()
            args.putString(Util.SHOW_ID_KEY, _id)
            val fragment = ShowDetailFragment()
            fragment.arguments = args
            return fragment
        }

        var showDetailCallback: ShowDetailCallback? = null
    }

    private var showId: String = ""
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

        progressbar.visibility = View.VISIBLE

        val showNulableId = arguments?.getString(Util.SHOW_ID_KEY)
        if (showNulableId != null)
            showId = showNulableId

        // get show details
        SingletonApi.service.getShow(showId).enqueue(object: Callback<Show>{
            override fun onFailure(call: Call<Show>, t: Throwable) {
                t.message?.let { toast(it) }
                error(t)
            }

            override fun onResponse(call: Call<Show>, response: Response<Show>) {
                if (response.isSuccessful) {
                    val showItem: ShowDetail? = response.body()?.data

                    if (showItem != null) {
                        showName = showItem.title
                        showDescription = showItem.description

                        // add description text
                        show_detail_description.text = showDescription

                        // set toolbar title
                        my_toolbar.title = showName
                        my_toolbar.setNavigationOnClickListener {
                            (context as MainFragmentActivity).supportFragmentManager.popBackStack()
                        }
                    }
                } else {
                    toast("Error! Something went wrong, please try again")
                }
            }
        })


        // get episode list
        SingletonApi.service.getEpisodes(showId).enqueue(object: Callback<Episodes>{
            override fun onFailure(call: Call<Episodes>, t: Throwable) {
                progressbar.visibility = View.GONE
                t.message?.let { toast(it) }
                error(t)
            }

            override fun onResponse(call: Call<Episodes>, response: Response<Episodes>) {
                progressbar.visibility = View.GONE
                if (response.isSuccessful) {
                    val episodes = response.body()?.data

                    if (episodes != null) {

                        if (episodes.size > 0) {

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
                    }
                }

            }

        })

        // add episode listeners
        setAddEpisodeListeners()
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
    fun onAddEpisode(showId: String, showName: String?, showDescription: String?)
}