package mezic.grega.hows_gregamezic

import androidx.room.Room
import mezic.grega.hows_gregamezic.database.ShowDatabase
import mezic.grega.hows_gregamezic.database.models.EpisodeModelDb
import mezic.grega.hows_gregamezic.database.models.ShowDetailModelDb
import mezic.grega.hows_gregamezic.database.models.ShowModelDb
import mezic.grega.hows_gregamezic.network.*
import mezic.grega.hows_gregamezic.network.errorInterfaces.NetworkErrorCallback
import mezic.grega.hows_gregamezic.network.otherNetworkInterfaces.NetworkAddEpisodeCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException
import java.util.concurrent.Executors

private const val FILENAME = "ShowsDb"

object ShowRepository {

    val database = Room.databaseBuilder(
        ShowApp.instance,
        ShowDatabase::class.java,
        FILENAME
    )
        .fallbackToDestructiveMigration()
        .build()

    // EXECUTOR
    private val executor = Executors.newSingleThreadExecutor()


    /**
     * SHOWS
     */
    fun getAllShows(callback: (List<ShowItem>?) -> Unit, errorCallback: (NetworkError) -> Unit) {
        SingletonApi.service.getShows().enqueue(object : Callback<Shows> {
            override fun onFailure(call: Call<Shows>, t: Throwable) {

                // read database
                executor.execute {
                    val itemsDb = database.showsDao().getAllShows()
                    var items: List<ShowItem>? = null
                    if (itemsDb != null)
                        items = itemsDb.map { ShowItem(it.id, it.title, it.imageUrl, it.likesCount) }
                    else

                        errorCallback(NetworkError("No data in the database"))

                    callback(items)
                }

                // check for error
                if (t is UnknownHostException) { // No internet exception, shows data from db
                    errorCallback(NetworkError("No internet connection"))
                } else {
                    errorCallback(NetworkError("Unknown error. Please try again"))
                }
            }

            override fun onResponse(call: Call<Shows>, response: Response<Shows>) {
                if (response.isSuccessful) {
                    val items = response.body()?.data
                    if (items != null) {
                        callback(items)

                        // add show list to database
                        val databaseItems = items.map {
                            ShowModelDb(it._id, it.title, it.imageUrl, it.likesCount)
                        }

                        //insert items to database
                        insertShowList(databaseItems)
                    }
                } else {
                    // read database
                    var items: List<ShowItem>? = null
                    executor.execute {
                        val itemsDb = database.showsDao().getAllShows()
                        if (itemsDb != null)
                            items = itemsDb.map { ShowItem(it.id, it.title, it.imageUrl, it.likesCount) }
                        else
                            errorCallback(NetworkError("No data in the database"))
                        callback(items)
                    }
                }
            }
        })
    }


    // insert show lists
    fun insertShowList(showList: List<ShowModelDb>) {
        executor.execute {
            database.showsDao().insertAllShows(showList)
        }
    }

    /**
     * SHOW DETAILS
     */
    // get show detail
    fun getShowDetail(showId: String, callback: (ShowDetail?) -> Unit, errorCallback: (NetworkError) -> Unit) {
        SingletonApi.service.getShow(showId).enqueue(object : Callback<Show> {

            override fun onFailure(call: Call<Show>, t: Throwable) {
                // read data from database
                var showDetail: ShowDetail? = null
                executor.execute {
                    val showDetailDb = database.showDetailDao().getShowDetail(showId)
                    if (showDetailDb != null) {
                        showDetail = ShowDetail(
                            showDetailDb.type,
                            showDetailDb.title,
                            showDetailDb.description,
                            showDetailDb.id,
                            showDetailDb.imageUrl,
                            showDetailDb.likesCount
                        )
                    } else
                        errorCallback(NetworkError("No data in the database"))

                    callback(showDetail)
                }

                if (t is UnknownHostException) { // Internet exception
                    errorCallback(NetworkError("No internet connection"))
                } else {
                    errorCallback(NetworkError("Unknown error. Please try again"))
                }
            }

            override fun onResponse(call: Call<Show>, response: Response<Show>) {
                if (response.isSuccessful) {
                    val showItem: ShowDetail? = response.body()?.data

                    if (showItem != null) {
                        callback(showItem)
                        //insert item to db
                        executor.execute { insertShowDetail(ShowDetailModelDb(
                                    showItem._id,
                                    showItem.type,
                                    showItem.description,
                                    showItem.title,
                                    showItem.imageUrl,
                                    showItem.likesCount
                                )
                            )
                        }
                    }
                } else {
                    var showDetail: ShowDetail? = null
                    executor.execute {
                        val showDetailDb = database.showDetailDao().getShowDetail(showId)
                        if (showDetailDb != null) {
                            showDetail = ShowDetail(
                                showDetailDb.type,
                                showDetailDb.title,
                                showDetailDb.description,
                                showDetailDb.id,
                                showDetailDb.imageUrl,
                                showDetailDb.likesCount
                            )
                        } else
                            errorCallback(NetworkError("No data in the database"))

                        callback(showDetail)
                    }
                }
            }
        })
    }


    fun insertShowDetail(showDetail: ShowDetailModelDb) {
        executor.execute {
            database.showDetailDao().insertShowDetail(showDetail)
        }
    }


    /**
     * EPISODES
     */
    // get all episodes
    fun getAllEpisodes(showId: String, callback: (List<EpisodeItem>?) -> Unit, errorCallback: (NetworkError) -> Unit) {
        SingletonApi.service.getEpisodes(showId).enqueue(object : Callback<Episodes> {
            override fun onFailure(call: Call<Episodes>, t: Throwable) {
                executor.execute {
                    val episodesDb = database.episodeDao().getAllShowsEpisodes(showId)
                    var episodes: List<EpisodeItem>? = null
                    if (episodesDb != null) {
                        episodes = episodesDb.map {
                            EpisodeItem(
                                it.show_id,
                                it.title,
                                it.description,
                                it.imageUrl,
                                it.episodeNumber,
                                it.season
                            )
                        }
                    } else
                        errorCallback(NetworkError("No data in the database"))

                    callback(episodes)
                }
                if (t is UnknownHostException) { // Internet exception
                    errorCallback(NetworkError("No internet connection"))
                } else {
                    errorCallback(NetworkError("Unknown error. Please try again"))
                }
            }

            override fun onResponse(call: Call<Episodes>, response: Response<Episodes>) {
                if (response.isSuccessful) {
                    val episodes = response.body()?.data

                    if (episodes != null) {
                        // update database
                        insertAllEpisodes(episodes.map {
                            EpisodeModelDb(
                                it._id,
                                showId,
                                it.title,
                                it.description,
                                it.imageUrl,
                                it.episodeNumber,
                                it.season
                            )
                        })

                    } else
                        errorCallback(NetworkError("No data in the database"))
                    callback(episodes)
                } else {
                    executor.execute {
                        val episodesDb = database.episodeDao().getAllShowsEpisodes(showId)
                        var episodes: List<EpisodeItem>? = null
                        if (episodesDb != null) {
                            episodes = episodesDb.map {
                                EpisodeItem(
                                    it.show_id,
                                    it.title,
                                    it.description,
                                    it.imageUrl,
                                    it.episodeNumber,
                                    it.season
                                )
                            }
                        } else
                            errorCallback(NetworkError("No data in the database"))

                        callback(episodes)
                    }
                }
            }
        })
    }

    // insert all episodes
    fun insertAllEpisodes(episodes: List<EpisodeModelDb>) {
        executor.execute {
            database.episodeDao().insertAllEpisodes(episodes)
        }
    }

    fun addEpisode(episode: AddEpisode, errorCallback: (NetworkError) -> Unit, episodeAddedCallback: (Boolean) -> Unit) {
        SingletonApi.service.addEpisode(ShowApp.mSharedPreferencesManager.getUserToken(), episode)
            .enqueue(object : Callback<AddEpisodeResult> {
                override fun onFailure(call: Call<AddEpisodeResult>, t: Throwable) {
                    episodeAddedCallback(false)
                    if (t is UnknownHostException) { // Internet exception
                        errorCallback(NetworkError("No internet connection"))
                    } else {
                        errorCallback(NetworkError("Unknown error. Please try again"))
                    }
                }

                override fun onResponse(
                    call: Call<AddEpisodeResult>,
                    response: Response<AddEpisodeResult>
                ) {
                    if (response.isSuccessful) {
                        episodeAddedCallback(true)
                    } else {
                        episodeAddedCallback(false)
                        errorCallback(NetworkError("Unknown error. Please try again"))
                    }
                }
            })
    }
}