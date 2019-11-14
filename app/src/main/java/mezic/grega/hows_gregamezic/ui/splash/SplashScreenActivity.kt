package mezic.grega.hows_gregamezic.ui.splash

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import kotlinx.android.synthetic.main.activity_splash_screen.*
import mezic.grega.hows_gregamezic.MainBaseActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.ui.login.LoginActivity

class SplashScreenActivity: MainBaseActivity() {

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
                    tv_splash.animate()
                        .translationY((height - imageHeight - 2*margin)/2 + tvHeight - 2*margin)
                        .setDuration(0)
                        .start()

                    // animate tv
                    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1f)
                    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f)
                    val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
                    tv_splash.visibility = View.VISIBLE
                    ObjectAnimator.ofPropertyValuesHolder(tv_splash, scaleX, scaleY, alpha).apply {
                        interpolator = OvershootInterpolator()
                        addListener(object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(animation: Animator?) {
                            }

                            override fun onAnimationEnd(animation: Animator?) {
                                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                                finish()
                            }

                            override fun onAnimationCancel(animation: Animator?) {
                            }

                            override fun onAnimationStart(animation: Animator?) {
                            }

                        })
                    }.start()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            }).startDelay = 10 // added a small delay, because it looks nicer
    }
}