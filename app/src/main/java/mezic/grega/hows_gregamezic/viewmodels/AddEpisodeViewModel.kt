package mezic.grega.hows_gregamezic.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mezic.grega.hows_gregamezic.ShowRepository
import mezic.grega.hows_gregamezic.network.AddEpisode
import mezic.grega.hows_gregamezic.network.MediaData
import mezic.grega.hows_gregamezic.network.NetworkError
import java.io.File

class AddEpisodeViewModel: ViewModel() {

    val error = MutableLiveData<NetworkError>()
    val success = MutableLiveData<Boolean>()


    val error_media = MutableLiveData<NetworkError>()
    val success_media = MutableLiveData<MediaData>()

    fun addEpisode(episode: AddEpisode) {
        ShowRepository.addEpisode(episode,
            {
                error.postValue(it)
            }, {
                success.postValue(it)
            })
    }

    fun uploadMedia(file: File) {
        ShowRepository.uploadImage(file, {networkError ->
            error_media.postValue(networkError)
        }, {
            success_media.postValue(it)
        })
    }


}