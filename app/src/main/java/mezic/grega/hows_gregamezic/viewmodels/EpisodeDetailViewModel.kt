package mezic.grega.hows_gregamezic.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mezic.grega.hows_gregamezic.ShowRepository
import mezic.grega.hows_gregamezic.network.EpisodeDetailData
import mezic.grega.hows_gregamezic.network.NetworkError

class EpisodeDetailViewModel : ViewModel() {

    val episodeDetail = MutableLiveData<EpisodeDetailData>()
    val error = MutableLiveData<NetworkError>()

    fun getEpisodeDetail(episodeId: String) {
        ShowRepository.getEpisodeDetail(episodeId, {episode ->
            episodeDetail.postValue(episode)
        }, {networkError ->
            error.postValue(networkError)
        })
    }
}