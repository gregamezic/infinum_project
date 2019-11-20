package mezic.grega.hows_gregamezic.utils

class Util {


    companion object {
        //REQUESTS
        const val EPISODE_ADD_EPISODE_REQUEST_CODE = 1
        const val PERMISSION_CAMERA_REQUEST_CODE = 2
        const val PERMISSION_READ_EXTERNAL_REQUEST_CODE = 3
        const val REQUEST_IMAGE_CAPTURE = 4
        const val REQUEST_IMAGE_GALLERY = 5

        //SHARED PREFERENCES
        const val SHARED_PREFERENCES_NAME = "mezic.grega.shows.SHARED_PREFERENCES_NAME"

        // LOGIN
        const val USERNAME_KEY = "mezic.grega.shows.USERNAME_KEY"

        // SHOWS
        const val SHOW_NAME_KEY = "mezic.grega.shows.SHOW_NAME_KEY"
        const val SHOW_ID_KEY = "mezic.grega.shows.SHOW_ID_KEY"
        const val SHOW_DESCRIPTION_KEY = "mezic.grega.shows.SHOW_DESCRIPTION_KEY"
        const val SHOW_YEAR_KEY = "mezic.grega.shows.SHOW_YEAR_KEY"
        const val SHOW_EPISODES_KEY = "mezic.grega.shows.SHOW_EPISODES_KEY"
        const val SHOW_POSITION = "mezic.grega.shows.SHOW_POSITION"

        //SHOW DETAIL LIKE
        const val SHOW_DETAIL_LIKE = 1
        const val SHOW_DETAIL_DISLIKE = -1
        const val SHOW_DETAIL_NEUTRAL = 0

        // EPISODE DETAIL
        const val EPISODE_ID_KEY = "mezic.grega.shows.EPISODE_ID_KEY"

        // ADD EPISODE
        const val EPISODE_ADD_EPISODE_KEY = "mezic.grega.shows.EPISODE_ADD_EPISODE_KEY"
        const val EPISODE_IMAGE_PATH_KEY = "mezic.grega.shows.EPISODE_ADD_EPISODE_KEY"
        const val EPISODE_NAME = "mezic.grega.shows.EPISODE_NAME"
        const val EPISODE_DESCRIPTION = "mezic.grega.shows.EPISODE_DESCRIPTION"
        const val EPISODE_SEASON_KEY = "mezic.grega.shows.EPISODE_SEASON_KEY"
        const val EPISODE_EPISODE_KEY = "mezic.grega.shows.EPISODE_EPISODE_KEY"


        // NUMBER PICKER DIALOG
        const val MIN_SEASON_VALUE = 1
        const val MAX_SEASON_VALUE = 20

        const val MIN_EPISODE_VALUE = 1
        const val MAX_EPISODE_VALUE = 99

        const val DEFAULT_NUMBER_PICKER_VALUE = 1


        const val BASE_URL = "https://api.infinum.academy/api/"

    }
}