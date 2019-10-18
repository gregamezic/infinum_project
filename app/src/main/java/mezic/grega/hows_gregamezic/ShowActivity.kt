package mezic.grega.hows_gregamezic

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_shows.*
import kotlinx.android.synthetic.main.activity_welcome.*

class ShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        // crate hardcoded shows list
        val shows = createList()

        showsRecycleView.layoutManager = LinearLayoutManager(this)
        showsRecycleView.adapter = ShowsAdapter(shows) {Toast.makeText(this, it.name, LENGTH_SHORT).show()}


    }

    private fun createList(): List<Show> {
        val episodes = mutableListOf<Episode>()

        return mutableListOf(
            Show(R.drawable.icon_tbbt, "The Big Bang Theory", "(2007 - 2019)", "A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory.", episodes),
            Show(R.drawable.icon_to, "The Office", "(2005 - 2013)", "A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium.", episodes),
            Show(R.drawable.icon_house, "House M.D.", "(2004 - 2012)", "An antisocial maverick doctor who specializes in diagnostic medicine does whatever it takes to solve puzzling cases that come his way using his crack team of doctors and his wits.", episodes),
            Show(R.drawable.icon_jtv, "Jane the Virgin", "(2004 - )", "A young, devout Catholic woman discovers that she was accidentally artificially inseminated.", episodes),
            Show(R.drawable.icon_sherlock, "Sherlock", "(2010 - )", "A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory.", episodes)



            /*Show("6", "The Big Bang Theory", "2007 - 2019", "A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory.", episodes),
            Show("7", "The Big Bang Theory", "2007 - 2019", "A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory.", episodes),
            Show("8", "The Big Bang Theory", "2007 - 2019", "A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory.", episodes),
            Show("9", "The Big Bang Theory", "2007 - 2019", "A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory.", episodes)*/
        )
    }
}