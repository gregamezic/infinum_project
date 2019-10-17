package mezic.grega.hows_gregamezic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class ShowActivity : AppCompatActivity() {

    val USERNAME_KEY : String = "USERNAME_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        //read message
        val message = intent.getStringExtra(USERNAME_KEY)
        tv_welcome_name.text = "Welcome $message"

    }
}