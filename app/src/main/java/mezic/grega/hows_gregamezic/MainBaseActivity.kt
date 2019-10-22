package mezic.grega.hows_gregamezic

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.view_toolbar.*

open class MainBaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun setupToolbar() {
        // set toolbar
        setSupportActionBar(my_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    protected fun setToolbarName(name: String) {
        title = name
    }

    protected fun toast(message: String, long: Boolean = false) {
        if (long)
            Toast.makeText(this, message, LENGTH_LONG).show()
        else
            Toast.makeText(this, message, LENGTH_SHORT).show()
    }
}