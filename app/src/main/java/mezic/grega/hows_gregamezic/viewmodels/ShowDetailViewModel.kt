package mezic.grega.hows_gregamezic.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.moshi.internal.Util
import mezic.grega.hows_gregamezic.ShowRepository
import mezic.grega.hows_gregamezic.network.EpisodeItem
import mezic.grega.hows_gregamezic.network.NetworkError
import mezic.grega.hows_gregamezic.network.ShowDetail
import mezic.grega.hows_gregamezic.utils.Util.Companion.SHOW_DETAIL_LIKE

class ShowDetailViewModel: ViewModel() {

    val showDetailItem = MutableLiveData<ShowDetail>()
    val showLikeItem = MutableLiveData<Int>()

    val callSuccessfull = MutableLiveData<Boolean>()

    val episodeList = MutableLiveData<List<EpisodeItem>>()
    val error = MutableLiveData<NetworkError>()

    fun getShowDetail(showId: String) {
        ShowRepository.getShowDetail(showId,
            { showDetailItem.postValue(it) },
            { error.postValue(it) })
        }

    fun getShowLike(showId: String) {
        ShowRepository.getShowLike(showId
        ) {showLikeItem.postValue(it)}
    }

    fun likeShow(showId: String) {
        ShowRepository.postLikeShow(showId, {
            if (it){
                callSuccessfull.postValue(true)
            } else {
                callSuccessfull.postValue(false)
            }
        }, {
            error.postValue(it)
        })
    }


    fun dislikeShow(showId: String) {
        ShowRepository.postDislikeShow(showId, {
            if (it){
                callSuccessfull.postValue(true)
            } else {
                callSuccessfull.postValue(false)
            }
        }, {
            error.postValue(it)
        })
    }


    fun getEpisodeList(showId: String) {
        ShowRepository.getAllEpisodes(showId, {
            episodeList.postValue(it)
        }, {
            error.postValue(it)
        })
    }
}
