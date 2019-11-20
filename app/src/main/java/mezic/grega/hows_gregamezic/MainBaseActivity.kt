package mezic.grega.hows_gregamezic

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.view_toolbar.*
import mezic.grega.hows_gregamezic.utils.SharedPreferencesManager

open class MainBaseActivity : AppCompatActivity() {

    lateinit var mSharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSharedPreferencesManager = ShowApp.mSharedPreferencesManager
    }

    protected fun setupToolbar(name: String) {
        my_toolbar1.title = name
        my_toolbar1.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    /**
     * Logger wrappers
     */
    // debugging
    protected fun logd(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    // information
    protected fun logi(tag: String, message: String, onlyDebug: Boolean = false) {
            if (onlyDebug && BuildConfig.DEBUG) {
                Log.i(tag, message)
            } else {
                Log.i(tag, message)
            }
    }

    protected fun loge(tag: String, message: String, throwable: Throwable? = null) {
        if (throwable != null)
            Log.e(tag, message, throwable)
        else
            Log.e(tag, message)
    }
}