package mezic.grega.hows_gregamezic.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mezic.grega.hows_gregamezic.ShowRepository
import mezic.grega.hows_gregamezic.network.AddEpisode
import mezic.grega.hows_gregamezic.network.NetworkError

class AddEpisodeViewModel: ViewModel() {

    val error = MutableLiveData<NetworkError>()
    val success = MutableLiveData<Boolean>()

    fun addEpisode(episode: AddEpisode) {
        ShowRepository.addEpisode(episode,
            {
                error.postValue(it)
            }, {
                success.postValue(it)
            })
    }

}