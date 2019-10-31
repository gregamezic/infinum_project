package mezic.grega.hows_gregamezic.shows

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_shows.*
import mezic.grega.hows_gregamezic.MainFragmentActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.shows.dummy.Show
import mezic.grega.hows_gregamezic.shows.dummy.ShowsAdapter
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.lang.RuntimeException

class ShowFragment : Fragment() {


    companion object {

        fun newIntent(): ShowFragment {
            return ShowFragment()
        }

        var showCallback: ShowCallback? = null
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

        showsRecycleView.layoutManager = LinearLayoutManager(context)
        showsRecycleView.adapter = ShowsAdapter(MainFragmentActivity.shows) {
            showCallback?.onShowItemClick(it.name, it.description, it.year)
        }
    }

    private fun logout() {
        showCallback?.onLogout()
    }
}


interface ShowCallback {
    fun onLogout()
    fun onShowItemClick(name: String, description: String, year: String)
}