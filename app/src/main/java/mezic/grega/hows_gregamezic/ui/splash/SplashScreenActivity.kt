package mezic.grega.hows_gregamezic.ui.splash

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import kotlinx.android.synthetic.main.activity_splash_screen.*
import mezic.grega.hows_gregamezic.MainBaseActivity
import mezic.grega.hows_gregamezic.MainFragmentActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.ui.login.LoginActivity

class SplashScreenActivity: MainBaseActivity() {

    private var mDelayHandler: Handler = Handler()
    private val splashDelay: Long = 300

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // get device dimensions
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val height = displayMetrics.heightPixels.toFloat()
        val imageHeight = img_splash_icon.drawable.intrinsicHeight
        val tvHeight = tv_splash.height
        val margin = resources.getDimension(R.dimen.default_padding)

        img_splash_icon.animate()
            .translationY((height - imageHeight - margin)/2)
            .setInterpolator(BounceInterpolator())
            .setDuration(800)
            .setListener(object: Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    // set the position of tv
                    tv_splash.visibility = View.VISIBLE
                    tv_splash.y = (height - imageHeight - 2*margin)/2 + tvHeight - 2*margin
                    tv_splash.alpha = 0f

                    // animate tv
                    tv_splash.animate()
                        .scaleX(3.5f)
                        .scaleY(3.5f)
                        .setInterpolator(OvershootInterpolator())
                        .alpha(1f)
                        .setListener(object: Animator.AnimatorListener {
                            override fun onAnimationRepeat(animation: Animator?) {
                            }
                            override fun onAnimationEnd(animation: Animator?) {

                                // open application with small delay
                                mDelayHandler.postDelayed(mRunnable, splashDelay)

                            }
                            override fun onAnimationCancel(animation: Animator?) {
                            }

                            override fun onAnimationStart(animation: Animator?) {
                            }
                        })
                }
                override fun onAnimationCancel(animation: Animator?) {
                }
                override fun onAnimationStart(animation: Animator?) {
                }

            }).startDelay = 80 // added a small delay, because it looks nicer
    }

    public override fun onStop() {
        super.onStop()
        mDelayHandler.removeCallbacks(mRunnable)
    }
}