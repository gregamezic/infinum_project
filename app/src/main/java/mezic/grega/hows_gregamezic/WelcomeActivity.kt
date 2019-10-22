package mezic.grega.hows_gregamezic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_welcome.*
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.shows.ShowActivity

class WelcomeActivity : AppCompatActivity() {

    val USERNAME_KEY : String = "USERNAME_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        //read message
        val message = intent.getStringExtra(USERNAME_KEY)
        tv_welcome_name.text = "Welcome $message"


        Handler().postDelayed(Runnable {
            startActivity(Intent(this, ShowActivity::class.java))
            finish()
        }, 2000)

    }
}
