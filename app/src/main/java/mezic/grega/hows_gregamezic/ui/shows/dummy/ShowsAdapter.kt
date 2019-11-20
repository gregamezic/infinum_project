package mezic.grega.hows_gregamezic.ui.shows.dummy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_show_item.view.*
import mezic.grega.hows_gregamezic.network.ShowItem
import com.squareup.picasso.Picasso

class ShowsAdapter(val clickAction: (ShowItem) -> Unit) :
    RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    private var dataset = listOf<ShowItem>()
    var isLinear = false


    fun setData(dataset: List<ShowItem>) {
        this.dataset = dataset
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val view = if (isLinear) {
            LayoutInflater.from(parent.context)
                .inflate(mezic.grega.hows_gregamezic.R.layout.view_show_item, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(mezic.grega.hows_gregamezic.R.layout.show_item_grid, parent, false)
        }
        return ShowViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(dataset[position])
    }


    inner class ShowViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: ShowItem) {

            with(itemView) {
                Picasso.get().load("https://api.infinum.academy${item.imageUrl}").into(image_show_item)
                tv_shows_title_item.text = item.title
                tv_shows_like_count_item?.text = item.likesCount.toString()
                setOnClickListener {clickAction(item)}
            }
        }
    }
}