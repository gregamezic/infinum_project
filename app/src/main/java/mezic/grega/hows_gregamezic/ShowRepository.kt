package mezic.grega.hows_gregamezic

import androidx.room.Room
import mezic.grega.hows_gregamezic.database.ShowDatabase
import mezic.grega.hows_gregamezic.database.models.EpisodeModelDb
import mezic.grega.hows_gregamezic.database.models.ShowDetailModelDb
import mezic.grega.hows_gregamezic.database.models.ShowModelDb
import mezic.grega.hows_gregamezic.network.*
import mezic.grega.hows_gregamezic.utils.Util
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
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
                    val items = getAllShowsDB()
                    if (items == null || items.isEmpty()) {
                        errorCallback(NetworkError("Error: no data in the database"))
                    }
                    callback(items)
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
                    } else {
                        errorCallback(NetworkError("Unknown error. Please try again!"))
                    }
                } else {
                    // read database
                    executor.execute {
                        val items = getAllShowsDB()
                        if (items == null || items.isEmpty()) {
                            errorCallback(NetworkError("Error: no data in the database"))
                        }
                        callback(items)
                    }
                }
            }
        })
    }

    fun getAllShowsDB(): List<ShowItem>? {
        // read database

        val itemsDb = database.showsDao().getAllShows()
        var items: List<ShowItem>? = null
        if (itemsDb.isNotEmpty()) {
            items = itemsDb.map { ShowItem(it.id, it.title, it.imageUrl, it.likesCount) }
        }
        return items
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
    fun getShowDetail(
        showId: String,
        callback: (ShowDetail?) -> Unit,
        errorCallback: (NetworkError) -> Unit
    ) {
        SingletonApi.service.getShow(showId).enqueue(object : Callback<Show> {

            override fun onFailure(call: Call<Show>, t: Throwable) {
                // read data from database
                executor.execute {
                    val showDetail = getShowDetailDB(showId)
                    if (showDetail == null) {
                        errorCallback(NetworkError("Error: no data in the database"))
                    }
                    callback(showDetail)
                }
            }

            override fun onResponse(call: Call<Show>, response: Response<Show>) {
                if (response.isSuccessful) {
                    val showItem: ShowDetail? = response.body()?.data

                    if (showItem != null) {
                        callback(showItem)
                        //insert item to db
                        executor.execute {
                            insertShowDetail(
                                ShowDetailModelDb(
                                    showItem._id,
                                    showItem.type,
                                    showItem.description,
                                    showItem.title,
                                    showItem.imageUrl,
                                    showItem.likesCount
                                )
                            )
                        }
                    } else {
                        errorCallback(NetworkError("Unknown error. Please try again!"))
                    }
                } else {
                    // read data from database
                    executor.execute {
                        val showDetail = getShowDetailDB(showId)
                        if (showDetail == null) {
                            errorCallback(NetworkError("Error: no data in the database"))
                        }
                        callback(showDetail)
                    }
                }
            }
        })
    }

    fun getShowDetailDB(showId: String): ShowDetail? {
        var showDetail: ShowDetail? = null
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
        }
        return showDetail
    }


    /**
     * SHOW LIKE/DISLIKE
     */
    fun getShowLike(showId: String, callback: (Int) -> Unit) {
        executor.execute {
            val like = database.showDetailDao().getShowLike(showId)
            callback(like)
        }
    }

    fun postLikeShow(
        showId: String,
        callback: (Boolean) -> Unit,
        errorCallback: (NetworkError) -> Unit
    ) {
        SingletonApi.service.likeShow(showId, ShowApp.mSharedPreferencesManager.getUserToken())
            .enqueue(object : Callback<ShowLikeDislikeResult> {


                override fun onFailure(call: Call<ShowLikeDislikeResult>, t: Throwable) {
                    callback(false)

                    if (t is UnknownHostException) {
                        errorCallback(NetworkError("No internet connection. Please try again!"))
                    } else {
                        errorCallback(NetworkError("Unknown error. Please try again!"))
                    }
                }

                override fun onResponse(
                    call: Call<ShowLikeDislikeResult>,
                    response: Response<ShowLikeDislikeResult>
                ) {
                    if (response.isSuccessful) {
                        callback(true)
                        executor.execute {
                            database.showDetailDao().updateLikes(showId, Util.SHOW_DETAIL_LIKE)
                        }
                    } else {
                        callback(false)
                        errorCallback(NetworkError("Unknown error. Please try again!"))
                    }
                }

            })
    }


    fun postDislikeShow(
        showId: String,
        callback: (Boolean) -> Unit,
        errorCallback: (NetworkError) -> Unit
    ) {
        SingletonApi.service.dislikeShow(showId, ShowApp.mSharedPreferencesManager.getUserToken())
            .enqueue(object : Callback<ShowLikeDislikeResult> {


                override fun onFailure(call: Call<ShowLikeDislikeResult>, t: Throwable) {
                    callback(false)

                    if (t is UnknownHostException) {
                        errorCallback(NetworkError("No internet connection. Please try again!"))
                    } else {
                        errorCallback(NetworkError("Unknown error. Please try again!"))
                    }
                }

                override fun onResponse(
                    call: Call<ShowLikeDislikeResult>,
                    response: Response<ShowLikeDislikeResult>
                ) {
                    if (response.isSuccessful) {
                        callback(true)
                        executor.execute {
                            database.showDetailDao().updateLikes(showId, Util.SHOW_DETAIL_DISLIKE)
                        }
                    } else {
                        callback(false)
                        errorCallback(NetworkError("Unknown error. Please try again!"))
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
    fun getAllEpisodes(
        showId: String,
        callback: (List<EpisodeItem>?) -> Unit,
        errorCallback: (NetworkError) -> Unit
    ) {
        SingletonApi.service.getEpisodes(showId).enqueue(object : Callback<Episodes> {
            override fun onFailure(call: Call<Episodes>, t: Throwable) {

                // read database
                executor.execute {
                    val episodes: List<EpisodeItem>? = getAllEpisodesDB(showId)
                    if (episodes == null || episodes.isEmpty()) {
                        errorCallback(NetworkError("Error: no data in the database"))
                    }
                    callback(episodes)
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
                    } else {
                        errorCallback(NetworkError("Unknown error. Please try again!"))
                    }
                    callback(episodes)
                } else {
                    // read database
                    executor.execute {
                        val episodes: List<EpisodeItem>? = getAllEpisodesDB(showId)
                        if (episodes == null || episodes.isEmpty()) {
                            errorCallback(NetworkError("Error: no data in the database"))
                        }
                        callback(episodes)
                    }
                }
            }
        })
    }

    fun getAllEpisodesDB(showId: String): List<EpisodeItem>? {
        val episodesDb = database.episodeDao().getAllShowsEpisodes(showId)
        var episodes: List<EpisodeItem>? = null
        if (episodesDb.isNotEmpty()) {
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
        }
        return episodes
    }

    // insert all episodes
    fun insertAllEpisodes(episodes: List<EpisodeModelDb>) {
        executor.execute {
            database.episodeDao().insertAllEpisodes(episodes)
        }
    }


    /**
     * EPISODE DETAIL
     */

    // todo: database
    fun getEpisodeDetail(
        episodeId: String,
        callback: (EpisodeDetailData?) -> Unit,
        errorCallback: (NetworkError) -> Unit
    ) {

        SingletonApi.service.getEpisodeDetail(episodeId).enqueue(object : Callback<EpisodeDetail> {
            override fun onFailure(call: Call<EpisodeDetail>, t: Throwable) {
                callback(null)

                if (t is UnknownHostException) {
                    errorCallback(NetworkError("No internet connection. Please try again!"))
                } else {
                    errorCallback(NetworkError("Unknown error. Please try again!"))
                }
            }

            override fun onResponse(call: Call<EpisodeDetail>, response: Response<EpisodeDetail>) {
                if (response.isSuccessful) {
                    val episodeDetail = response.body()?.data
                    callback(episodeDetail)
                } else {
                    errorCallback(NetworkError("Unknown error. Please try again!"))
                }
            }
        })
    }

    fun addEpisode(
        episode: AddEpisode,
        errorCallback: (NetworkError) -> Unit,
        episodeAddedCallback: (Boolean) -> Unit
    ) {
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

    /**
     * GET COMMENTS
     */

    fun getComments(
        episodeId: String,
        callback: (List<Comment>?) -> Unit,
        errorCallback: (NetworkError) -> Unit
    ) {

        SingletonApi.service.getComments(episodeId).enqueue(object : Callback<CommentResult> {
            override fun onFailure(call: Call<CommentResult>, t: Throwable) {
                callback(null)

                if (t is UnknownHostException) {
                    errorCallback(NetworkError("No internet connection. Please try again"))
                } else {
                    errorCallback(NetworkError("Unknown error. Please try again"))
                }
            }

            override fun onResponse(call: Call<CommentResult>, response: Response<CommentResult>) {
                if (response.isSuccessful) {
                    val comments = response.body()?.data

                    // return comments
                    callback(comments)
                } else {
                    callback(null)
                    errorCallback(NetworkError("Unknown error. Please try again"))
                }
            }
        })
    }

    /**
     * POST COMMENT
     */
    fun postComment(comment: CommentPost,
                    callback: (CommentPostResultData?) -> Unit,
                    errorCallback: (NetworkError) -> Unit) {
        SingletonApi.service.postComment(
            ShowApp.mSharedPreferencesManager.getUserToken(), comment).enqueue(
            object : Callback<CommentPostResult> {

                override fun onFailure(call: Call<CommentPostResult>, t: Throwable) {
                    callback(null)

                    if (t is UnknownHostException) {
                        errorCallback(NetworkError("No internet connection. Please try again"))
                    } else {
                        errorCallback(NetworkError("Unknown error. Please try again"))
                    }
                }

                override fun onResponse(
                    call: Call<CommentPostResult>,
                    response: Response<CommentPostResult>
                ) {
                    if (response.isSuccessful) {
                        val comment = response.body()?.data

                        // return comments
                        callback(comment)
                    } else {
                        callback(null)
                        errorCallback(NetworkError("Unknown error. Please try again"))
                    }
                }
            })
    }

    fun uploadImage(
        file: File,
        errorCallback: (NetworkError) -> Unit,
        callback: (MediaData?) -> Unit
    ) {

        SingletonApi.service.uploadMedia(
            file.asRequestBody("image/jpg".toMediaTypeOrNull()),
            ShowApp.mSharedPreferencesManager.getUserToken()
        )
            .enqueue(object : Callback<Media> {
                override fun onFailure(call: Call<Media>, t: Throwable) {
                    callback(null)
                    errorCallback(NetworkError("Unknown error. Please try again"))
                }

                override fun onResponse(call: Call<Media>, response: Response<Media>) {
                    if (response.isSuccessful) {
                        callback(response.body()?.data)
                    } else {
                        callback(null)
                        errorCallback(NetworkError("Unknown error. Please try again"))
                    }
                }
            })
    }
}