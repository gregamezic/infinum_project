package mezic.grega.hows_gregamezic.shows

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_shows.*
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.network.Shows
import mezic.grega.hows_gregamezic.network.SingletonApi
import mezic.grega.hows_gregamezic.shows.dummy.ShowsAdapter
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

class ShowFragment : Fragment() {


    companion object {

        fun newIntent(): ShowFragment {
            return ShowFragment()
        }

        lateinit var showCallback: ShowCallback
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        SingletonApi.service.getShows().enqueue(object : Callback<Shows> {
            override fun onFailure(call: Call<Shows>, t: Throwable) {
                progressbar.visibility = View.GONE
                t.message?.let { toast(it) }
                error(t)
            }

            override fun onResponse(call: Call<Shows>, response: Response<Shows>) {
                progressbar.visibility = View.GONE
                if (response.isSuccessful) {
                    val items = response.body()?.data
                    if (items != null) {
                        showsRecycleView.layoutManager = LinearLayoutManager(context)
                        showsRecycleView.adapter = ShowsAdapter(items) {
                            showCallback.onShowItemClick(it._id)
                        }
                    }
                } else {
                    toast("Error! Something went wrong, please try again")
                }
            }

        })
    }


    private fun logout() {
        showCallback.onLogout()
    }
}


interface ShowCallback {
    fun onLogout()
    fun onShowItemClick(_id: String)
}