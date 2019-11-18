package mezic.grega.hows_gregamezic.ui.shows

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_shows.*
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.database.models.ShowModelDb
import mezic.grega.hows_gregamezic.ShowRepository
import mezic.grega.hows_gregamezic.network.ShowItem
import mezic.grega.hows_gregamezic.network.Shows
import mezic.grega.hows_gregamezic.network.SingletonApi
import mezic.grega.hows_gregamezic.ui.shows.dummy.ShowsAdapter
import mezic.grega.hows_gregamezic.viewmodels.ShowViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.RuntimeException

class ShowFragment : Fragment() {

    /**
     * My vars
     */
    companion object {
        fun newIntent(): ShowFragment {
            return ShowFragment()
        }
        private lateinit var adapter: ShowsAdapter
        private lateinit var shows: List<ShowItem>
        lateinit var showCallback: ShowCallback
        lateinit var viewModel: ShowViewModel
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)

        // get view model
        viewModel = ViewModelProviders.of(this).get(ShowViewModel::class.java)

        // init interface
        if (context is ShowCallback) {
            showCallback = context
        } else {
            throw RuntimeException("Please implement ShowCallback")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shows, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_logout.setOnClickListener {
            context?.alert(Appcompat, "Are you sure you want to logout?", "Logout") {
                yesButton {
                    logout()
                }
                noButton { dialog ->
                    dialog.dismiss()
                }
            }?.show()
        }

        progressbar.visibility = View.VISIBLE
        adapter = ShowsAdapter { showCallback.onShowItemClick(it._id) }
        adapter.isLinear = true
        showsRecycleView.layoutManager = LinearLayoutManager(context)
        showsRecycleView.adapter = adapter

        // get the data
        viewModel.showsList.observe(this, Observer {
            progressbar.visibility = View.GONE
            if (it != null) {
                shows = it
                adapter.setData(shows)
            }
        })

        // check for errors
        viewModel.error.observe(this, Observer {
            toast(it.errorMessage)
        })


        fab_toggle_show_view.setOnClickListener {
            toggleView(adapter.isLinear)
        }
    }


    private fun logout() {
        showCallback.onLogout()
    }

    private fun toggleView(isLinear: Boolean) {
        if (isLinear) {
            adapter = ShowsAdapter { showCallback.onShowItemClick(it._id) }
            adapter.isLinear = false
            adapter.setData(shows)
            fab_toggle_show_view.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_listview))
            showsRecycleView.layoutManager = GridLayoutManager(context, 2)
            showsRecycleView.adapter = adapter

        } else {
            adapter = ShowsAdapter { showCallback.onShowItemClick(it._id) }
            adapter.isLinear = true
            adapter.setData(shows)
            fab_toggle_show_view.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_gridview_white))
            showsRecycleView.layoutManager = LinearLayoutManager(context)
            showsRecycleView.adapter = adapter
        }
    }
}


interface ShowCallback {
    fun onLogout()
    fun onShowItemClick(_id: String)
}