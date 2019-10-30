package mezic.grega.hows_gregamezic.utils

import android.content.SharedPreferences

class SharedPreferencesManager (private val sharedPreferences: SharedPreferences) {

    companion object {
        // KEYS
        const val IS_USER_LOGIN = "mezic.grega.hows_gregamezic.utils.IS_USER_LOGIN"

        // DEFAULT VALUES
        const val DEFAULT_LOGIN = false
    }


    /**
     * Functions for editing and reading the shared prefs
     */
    fun isUserLogin(): Boolean {
        return sharedPreferences.getBoolean(IS_USER_LOGIN, DEFAULT_LOGIN)
    }

    fun setUserLogin(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(IS_USER_LOGIN, isLoggedIn).apply()
    }

}