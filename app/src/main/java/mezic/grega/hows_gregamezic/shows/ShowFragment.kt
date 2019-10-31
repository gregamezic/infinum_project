package mezic.grega.hows_gregamezic.shows

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_shows.*
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


        val shows = mutableListOf(
            Show(
                R.drawable.icon_tbbt,
                "The Big Bang Theory",
                "(2007 - 2019)",
                "A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory."
            ),
            Show(
                R.drawable.icon_to,
                "The Office",
                "(2005 - 2013)",
                "A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium."
            ),
            Show(
                R.drawable.icon_house,
                "House M.D.",
                "(2004 - 2012)",
                "An antisocial maverick doctor who specializes in diagnostic medicine does whatever it takes to solve puzzling cases that come his way using his crack team of doctors and his wits."
            ),
            Show(
                R.drawable.icon_jtv,
                "Jane the Virgin",
                "(2004 - )",
                "A young, devout Catholic woman discovers that she was accidentally artificially inseminated."
            ),
            Show(
                R.drawable.icon_sherlock,
                "Sherlock",
                "(2010 - )",
                "A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory."
            )
        )
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
        showsRecycleView.adapter = ShowsAdapter(ShowActivity.shows) {
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