package mezic.grega.hows_gregamezic.network

import retrofit2.Call
import retrofit2.http.*
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST

interface ApiService {

    @POST("/api/users")
    fun registerUser(
        @Body userRegister: UserRegister
    ): Call<UserRegisterResult>

    @POST("/api/users/sessions")
    fun loginUser(
        @Body userLogin: UserRegister
    ): Call<UserLoginResult>

    @GET("/api/shows")
    fun getShows(): Call<Shows>

    @GET("/api/shows/{showId}")
    fun getShow(
        @Path("showId") showId: String
    ): Call<Show>

    @GET("/api/shows/{showId}/episodes")
    fun getEpisodes(
        @Path("showId") showId: String
    ): Call<Episodes>

    @POST("/api/episodes")
    fun addEpisode(
        @Header("Authorization") authorization: String,
        @Body addEpisode: AddEpisode
    ): Call<AddEpisodeResult>

    @POST("/api/shows/{showId}/like")
    fun likeShow(
        @Path("showId") showId: String,
        @Header("Authorization") authorization: String
    ): Call<ShowLikeDislikeResult>

    @POST("/api/shows/{showId}/dislike")
    fun dislikeShow(
        @Path("showId") showId: String,
        @Header("Authorization") authorization: String
    ): Call<ShowLikeDislikeResult>

    @GET("/api/episodes/{episodeId}")
    fun getEpisodeDetail(
        @Path("episodeId") episodeId: String
    ): Call<EpisodeDetail>

    @POST("/api/media")
    @Multipart
    fun uploadMedia(
        @Part("file\"; filename=\"image.jpg\"") request: RequestBody,
        @Header("Authorization") authorization: String
    ): Call<Media>

    @GET("/api/episodes/{episodeId}/comments")
    fun getComments(
        @Path("episodeId") episodeId: String
    ): Call<CommentResult>

    @POST("/api/comments")
    fun postComment(
        @Header("Authorization") authorization: String,
        @Body comment: CommentPost
    ): Call<CommentPostResult>
}