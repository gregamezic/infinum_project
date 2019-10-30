package mezic.grega.hows_gregamezic

import android.app.Application
import android.content.Context
import mezic.grega.hows_gregamezic.utils.SharedPreferencesManager
import mezic.grega.hows_gregamezic.utils.Util

class ShowApp : Application() {

    lateinit var mSharedPreferencesManager: SharedPreferencesManager

    override fun onCreate() {
        super.onCreate()
        setupSharedPreferences()
    }

    private fun setupSharedPreferences() {
        val sp = getSharedPreferences(Util.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        mSharedPreferencesManager = SharedPreferencesManager(sp)
    }
}