package mezic.grega.hows_gregamezic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_show_item.view.*

class ShowsAdapter(private val dataset: List<Show>, val action: (Show) -> Unit) :
    RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsAdapter.ShowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_show_item, parent, false)
        return ShowViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ShowsAdapter.ShowViewHolder, position: Int) {
        holder.bind(dataset[position])
    }


    inner class ShowViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Show) {
            itemView.image_show_item.setImageResource(item.id)
            itemView.tv_shows_title_item.text = item.name
            itemView.tv_shows_date_item.text = item.year
            itemView.setOnClickListener {action(item)}
        }
    }
}