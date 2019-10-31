package mezic.grega.hows_gregamezic.episodes.dummy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_episode_item.view.*
import mezic.grega.hows_gregamezic.R

class EpisodeAdapter(private val dataset: MutableList<Episode>, val action: (Episode) -> Unit) : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {
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
    /*fun addEpisode(episode: Episode) {
        dataset.add(episode)
        notifyItemInserted(dataset.size-1)
    }*/

    inner class EpisodeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Episode) {
            itemView.tv_item_episode_name.text = item.name
            itemView.tv_item_season_episode.text = item.episode_season
            itemView.setOnClickListener {action(item)}
        }
    }
}