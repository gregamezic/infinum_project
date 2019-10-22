package mezic.grega.hows_gregamezic.shows

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_shows.*
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.Util
import mezic.grega.hows_gregamezic.shows.dummy.Show
import mezic.grega.hows_gregamezic.shows.dummy.ShowsAdapter

class ShowActivity : AppCompatActivity() {

    companion object {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        showsRecycleView.layoutManager = LinearLayoutManager(this)
        showsRecycleView.adapter = ShowsAdapter(shows) {
            val intent = Intent(this, ShowDetailActivity::class.java)
            intent.putExtra(Util().SHOW_NAME_KEY, it.name)
            intent.putExtra(Util().SHOW_DESCRIPTION_KEY, it.description)
            intent.putExtra(Util().SHOW_YEAR_KEY, it.year)
            startActivity(intent)
        }
    }

}