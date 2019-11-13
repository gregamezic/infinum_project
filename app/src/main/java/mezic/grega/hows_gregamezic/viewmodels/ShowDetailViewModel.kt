package mezic.grega.hows_gregamezic.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mezic.grega.hows_gregamezic.ShowRepository
import mezic.grega.hows_gregamezic.network.EpisodeItem
import mezic.grega.hows_gregamezic.network.NetworkError
import mezic.grega.hows_gregamezic.network.ShowDetail

class ShowDetailViewModel: ViewModel() {

    val showDetailItem = MutableLiveData<ShowDetail>()
    val episodeList = MutableLiveData<List<EpisodeItem>>()
    val error = MutableLiveData<NetworkError>()

    fun getShowDetail(showId: String) {
        ShowRepository.getShowDetail(showId,
            { showDetailItem.postValue(it) },
            { error.postValue(it) })
        }

    fun getEpisodeList(showId: String) {
        ShowRepository.getAllEpisodes(showId, {
            episodeList.postValue(it)
        }, {
            error.postValue(it)
        })
    }
}
