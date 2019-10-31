package mezic.grega.hows_gregamezic.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_welcome.*
import mezic.grega.hows_gregamezic.MainBaseActivity
import mezic.grega.hows_gregamezic.MainFragmentActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.utils.Util.Companion.USERNAME_KEY

class WelcomeActivity : MainBaseActivity() {

    private var mDelayHandler: Handler? = null
    private val splashDelay: Long = 2000 //2 seconds


    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            //val intent = Intent(applicationContext, ShowActivity::class.java)
            val intent = Intent(applicationContext, MainFragmentActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        //read message
        val message = intent.getStringExtra(USERNAME_KEY)
        tv_welcome_name.text = "Welcome $message"
    }



    override fun onStart() {
        super.onStart()

        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, splashDelay)
    }

    public override fun onStop() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onStop()
    }
}
