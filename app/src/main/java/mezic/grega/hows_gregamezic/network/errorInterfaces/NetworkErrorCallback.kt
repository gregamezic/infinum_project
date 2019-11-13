package mezic.grega.hows_gregamezic.network.errorInterfaces

import mezic.grega.hows_gregamezic.network.NetworkError

interface NetworkErrorCallback {
    fun onNetworkError(error: NetworkError)
}