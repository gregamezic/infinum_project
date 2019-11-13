package mezic.grega.hows_gregamezic

import android.app.Application
import android.content.Context
import mezic.grega.hows_gregamezic.utils.SharedPreferencesManager
import mezic.grega.hows_gregamezic.utils.Util

class ShowApp : Application() {

    companion object {
        lateinit var instance: ShowApp
        lateinit var mSharedPreferencesManager: SharedPreferencesManager
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        setupSharedPreferences()
    }

    private fun setupSharedPreferences() {
        val sp = getSharedPreferences(Util.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        mSharedPreferencesManager = SharedPreferencesManager(sp)
    }
}