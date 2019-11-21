package mezic.grega.hows_gregamezic.ui.comments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.view_toolbar.*
import mezic.grega.hows_gregamezic.MainFragmentActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.ui.comments.dummy.CommentsAdapter
import mezic.grega.hows_gregamezic.utils.Util
import mezic.grega.hows_gregamezic.viewmodels.CommentsViewModel
import org.jetbrains.anko.support.v4.toast
import android.view.inputmethod.InputMethodManager


class CommentsFragment: Fragment() {

    companion object {
        fun newIntent(episodeId: String): CommentsFragment {
            val args = Bundle()
            args.putString(Util.EPISODE_ID_KEY, episodeId)
            val fragment = CommentsFragment()
            fragment.arguments = args
            return fragment
        }

        private lateinit var adapter: CommentsAdapter
        private lateinit var viewModel: CommentsViewModel
        private lateinit var episodeId: String
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        viewModel = ViewModelProviders.of(this).get(CommentsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        episodeId = arguments?.getString(Util.EPISODE_ID_KEY, "").toString()

        // enable navigation anyway
        my_toolbar1.setNavigationOnClickListener {
            (context as MainFragmentActivity).supportFragmentManager.popBackStack()
        }

        comments_swipe_refresh.setOnRefreshListener {
            updateComments(episodeId)
        }

        adapter = CommentsAdapter()
        comments_recycle_view.layoutManager = LinearLayoutManager(context)
        comments_recycle_view.adapter = adapter

        // update comments list
        updateComments(episodeId)

        // post comment
        tv_post_comment.setOnClickListener {
            val textComment = et_comment.text.toString()
            if (textComment.isNotBlank()) {
                postComment(textComment)
            }
        }
    }

    private fun postComment(textComment: String) {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(requireActivity().currentFocus.windowToken, 0)

        viewModel.postComment(episodeId, textComment)
        viewModel.postSuccess.observe(this, Observer {
            if (it) {
                toast("Comment posted successfully!")
                updateComments(episodeId)
            } else {
                viewModel.error.observe(this, Observer {networkError ->
                    toast(networkError.errorMessage)
                })
            }
        })
    }

    private fun updateComments(episodeId: String){

        // start progress bar
        comments_swipe_refresh.isRefreshing = true

        // get comments
        viewModel.getComments(episodeId)
        viewModel.comments.observe(this, Observer {
            comments_swipe_refresh.isRefreshing = false
            if (it != null) {
                if (it.isNotEmpty()) {
                    et_comment.setText("")
                    comments_recycle_view.visibility = View.VISIBLE
                    linear_no_comments.visibility = View.GONE
                    adapter.setData(it)
                } else {
                    comments_recycle_view.visibility = View.GONE
                    linear_no_comments.visibility = View.VISIBLE
                }
            } else {
                viewModel.error.observe(this, Observer {networkError ->
                    toast(networkError.errorMessage)
                })
            }
        })
    }
}