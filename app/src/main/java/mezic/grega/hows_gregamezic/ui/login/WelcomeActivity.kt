package mezic.grega.hows_gregamezic.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_welcome.*
import mezic.grega.hows_gregamezic.MainBaseActivity
import mezic.grega.hows_gregamezic.MainFragmentActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.utils.Util.Companion.USERNAME_KEY

class WelcomeActivity : MainBaseActivity() {

    private var mDelayHandler: Handler = Handler()
    private val splashDelay: Long = 2000 //2 seconds


    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val intent = Intent(applicationContext, MainFragmentActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        //read message
        val messageEmail = intent.getStringExtra(USERNAME_KEY)
        val username = if (messageEmail.contains("@")) messageEmail.split("@")[0] else messageEmail
        tv_welcome_name.text = "Welcome $username"
    }


    override fun onStart() {
        super.onStart()
        //Navigate with delay
        mDelayHandler.postDelayed(mRunnable, splashDelay)
    }

    public override fun onStop() {
        super.onStop()
        mDelayHandler.removeCallbacks(mRunnable)
    }
}
