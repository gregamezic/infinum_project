package mezic.grega.hows_gregamezic.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * USERS
 */
@JsonClass(generateAdapter = false)
class UserRegister(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

class UserRegisterResult(
   val data: Data
)

class Data(
    val type :String,
    val email :String,
    val _id :String
)


/**
 * LOGIN
 */
class UserLoginResult(
    val data: DataLogin
)

class DataLogin(
    val token :String
)

/**
 * SHOWS
 */
class Shows(
    val data: List<ShowItem>
)

class ShowItem(
    val _id: String,
    val title: String,
    val imageUrl: String,
    val likesCount: Int
)

/**
 * Show detail
 */
class Show(
    val data: ShowDetail
)

class ShowDetail(
    val type: String,
    val title: String,
    val description: String,
    val _id: String,
    val imageUrl: String,
    val likesCount: Int
)


/**
 * Episodes
 */
class Episodes(
    val data: List<EpisodeItem>
)

class EpisodeItem(
    val _id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val episodeNumber: String,
    val season: String
)

@JsonClass(generateAdapter = false)
class AddEpisode(
    @Json(name = "showId") val showId: String,
    @Json(name = "mediaId") val mediaId: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "episodeNumber") val episodeNumber: String,
    @Json(name = "season") val season: String
)

class AddEpisodeResult(
    val message: String
)