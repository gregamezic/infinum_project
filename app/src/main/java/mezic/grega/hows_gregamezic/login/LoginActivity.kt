package mezic.grega.hows_gregamezic.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import kotlinx.android.synthetic.main.activity_login.*
import mezic.grega.hows_gregamezic.MainBaseActivity
import mezic.grega.hows_gregamezic.MainFragmentActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.utils.Util

class LoginActivity : MainBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // check if user is already logged in
        if (mSharedPreferencesManager.isUserLogin()) {
            startActivity(Intent(this, MainFragmentActivity::class.java))
            finish()
        }

        btn_login.isEnabled = false

        // On button login click action
        btn_login.setOnClickListener {
            //check if login input is correct
            if (isLoginValid()) {
                login()
            } else {
                Toast.makeText(this, "Retry to login!", LENGTH_SHORT).show()
            }
        }

        //set custom text change listeners
        input_username.addTextChangedListener(LoginTextWatcher())
        input_password.addTextChangedListener(LoginTextWatcher())
    }

    private fun login() {
        progressbar.visibility = View.VISIBLE

        Handler().postDelayed({
            val username: String = input_username.text.toString()
            val intent : Intent = Intent(this, WelcomeActivity::class.java).apply {
                putExtra(Util.USERNAME_KEY, username)
            }

            // save checked state to shared prefs
            mSharedPreferencesManager.setUserLogin(cb_remember_login.isChecked)

            progressbar.visibility = View.GONE

            // start the activity
            startActivity(intent)
            finish()
        }, 2500)
    }

    private fun isLoginValid(): Boolean {
        return validateUsername() && validatePassword()
    }

    private fun validateUsername(): Boolean {
        val username = input_username.text.toString()
        if (username.trim().isEmpty()) {
            input_layout_username.error = "Username must contain at least one character!"
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            input_layout_username.error = "Enter correct email address!"
            return false
        }

        input_layout_username.error = null
        input_layout_password.error = null
        return true
    }

    private fun validatePassword(): Boolean {
        val password = input_password.text.toString()
        if (password.trim().length < 6) {
            input_layout_password.error = "Password must contain at least 6 character!"
            return false
        }
        return true
    }



    inner class LoginTextWatcher : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            btn_login.isEnabled = isLoginValid()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}