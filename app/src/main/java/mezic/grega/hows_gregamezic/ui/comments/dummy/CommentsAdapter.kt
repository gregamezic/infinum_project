package mezic.grega.hows_gregamezic.ui.comments.dummy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_comment_view.view.*
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.network.Comment

class CommentsAdapter:
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private var dataset = listOf<Comment>()

    fun setData(dataset: List<Comment>){
        this.dataset = dataset
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment_view, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(dataset[position])
    }


    inner class CommentViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(item: Comment) {
            with(itemView){
                tv_comment_username.text = item.userEmail
                tv_comment_text_item.text = item.text
            }
        }
    }
}