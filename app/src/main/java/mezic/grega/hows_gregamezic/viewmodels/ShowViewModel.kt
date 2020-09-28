package mezic.grega.hows_gregamezic.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mezic.grega.hows_gregamezic.ShowRepository
import mezic.grega.hows_gregamezic.network.NetworkError
import mezic.grega.hows_gregamezic.network.ShowItem

class ShowViewModel : ViewModel() {

    init {
        ShowRepository.getAllShows({items ->
            showsList.postValue(items)
        }, {networkError ->
            error.postValue(networkError)
        })
    }

    val showsList = MutableLiveData<List<ShowItem>>()
    val error = MutableLiveData<NetworkError>()

}