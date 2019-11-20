package mezic.grega.hows_gregamezic.ui.episodes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_episode_detail.*
import kotlinx.android.synthetic.main.fragment_episode_detail.img_collapsing_toolbar
import kotlinx.android.synthetic.main.fragment_episode_detail.my_toolbar
import kotlinx.android.synthetic.main.fragment_show_detail.*
import kotlinx.android.synthetic.main.fragment_show_detail.progressbar
import mezic.grega.hows_gregamezic.MainFragmentActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.utils.Util.Companion.EPISODE_ID_KEY
import mezic.grega.hows_gregamezic.viewmodels.EpisodeDetailViewModel
import org.jetbrains.anko.support.v4.toast

class EpisodesDetailsFragment : Fragment() {

    companion object {
        fun newIntent(episodeId: String): EpisodesDetailsFragment {
            val args = Bundle()
            args.putString(EPISODE_ID_KEY, episodeId)
            val fragment = EpisodesDetailsFragment()
            fragment.arguments = args
            return fragment
        }

        lateinit var viewModel: EpisodeDetailViewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(this).get(EpisodeDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_episode_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressbar.visibility = View.VISIBLE

        val episodeId = arguments?.getString(EPISODE_ID_KEY, "").toString()

        // get episode details
        viewModel.getEpisodeDetail(episodeId)
        viewModel.episodeDetail.observe(this, Observer {
            progressbar.visibility = View.GONE
            if (it != null) {
                if (it.imageUrl.isNotBlank()) {
                    Picasso.get().load("https://api.infinum.academy${it.imageUrl}")
                        .into(img_collapsing_toolbar)
                } else {
                    img_collapsing_toolbar.visibility = View.GONE
                }
                tv_episode_detail_title.text = it.title
                tv_episode_detail_season_episode.text = "${it.season} ${it.episodeNumber}"
                tv_episode_detail_description.text = it.description
            } else {
                viewModel.error.observe(this, Observer {networkError ->
                    toast(networkError.errorMessage)
                })
            }
        })

        // enable navigation anyway
        my_toolbar.setNavigationOnClickListener {
            (context as MainFragmentActivity).supportFragmentManager.popBackStack()
        }

        // click listeners for comments
        img_comments.setOnClickListener {
            openComments()
        }
        tv_episode_detail_comments.setOnClickListener {
            openComments()
        }
    }

    private fun openComments() {
        toast("Open comments")
    }
}