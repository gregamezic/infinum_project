package mezic.grega.hows_gregamezic

import android.os.Bundle
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
        mSharedPreferencesManager = (application as ShowApp).mSharedPreferencesManager
    }

    protected fun setupToolbar(name: String) {
        my_toolbar.title = name
        my_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    protected fun toast(message: String, long: Boolean = false) {
        if (long)
            Toast.makeText(this, message, LENGTH_LONG).show()
        else
            Toast.makeText(this, message, LENGTH_SHORT).show()
    }
}