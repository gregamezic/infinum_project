package mezic.grega.hows_gregamezic.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mezic.grega.hows_gregamezic.ShowRepository
import mezic.grega.hows_gregamezic.network.Comment
import mezic.grega.hows_gregamezic.network.CommentPost
import mezic.grega.hows_gregamezic.network.NetworkError


class CommentsViewModel : ViewModel() {

    val comments = MutableLiveData<List<Comment>>()
    val error = MutableLiveData<NetworkError>()

    val postSuccess = MutableLiveData<Boolean>()


    fun getComments(episodeId: String) {
        ShowRepository.getComments(episodeId, {
            comments.postValue(it)
        }, { networkError ->
            error.postValue(networkError)
        })
    }

    fun postComment(episodeId: String, text: String) {
        ShowRepository.postComment(CommentPost(episodeId, text), {
            postSuccess.postValue(it != null)
        }, {networkError ->
            error.postValue(networkError)
        })
    }
}