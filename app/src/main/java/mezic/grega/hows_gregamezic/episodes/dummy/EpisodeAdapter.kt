package mezic.grega.hows_gregamezic.episodes.dummy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_episode_item.view.*
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.network.EpisodeItem

class EpisodeAdapter(private val dataset: List<EpisodeItem>, val action: (EpisodeItem) -> Unit) : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_episode_item, parent, false)
        return EpisodeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(dataset[position])
    }


    inner class EpisodeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: EpisodeItem) {
            itemView.tv_item_episode_name.text = item.title
            itemView.tv_item_season_episode.text = "${item.season.trim()} ${item.episodeNumber.trim()}"
            itemView.setOnClickListener {action(item)}
        }
    }
}