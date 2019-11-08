package mezic.grega.hows_gregamezic.utils

import android.content.SharedPreferences

class SharedPreferencesManager (private val sharedPreferences: SharedPreferences) {

    companion object {
        // KEYS
        const val IS_USER_LOGIN = "mezic.grega.hows_gregamezic.utils.IS_USER_LOGIN"
        const val USER_TOKEN = "mezic.grega.hows_gregamezic.utils.USER_TOKEN"

        // DEFAULT VALUES
        const val DEFAULT_LOGIN = false
        const val DEFAULT_TOKEN: String = ""
    }


    /**
     * Functions for editing and reading the shared prefs
     */


    /**
     * Login
     */
    fun isUserLogin(): Boolean {
        return sharedPreferences.getBoolean(IS_USER_LOGIN, DEFAULT_LOGIN)
    }

    fun setUserLogin(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(IS_USER_LOGIN, isLoggedIn).apply()
    }



    /**
     * User token for API
     */
    fun getUserToken(): String {
        return sharedPreferences.getString(USER_TOKEN, DEFAULT_TOKEN)
    }

    fun setUserToken(token: String?) {
        sharedPreferences.edit().putString(USER_TOKEN, token).apply()
    }

}