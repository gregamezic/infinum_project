package mezic.grega.hows_gregamezic.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.progressbar
import kotlinx.android.synthetic.main.fragment_shows.*
import mezic.grega.hows_gregamezic.MainBaseActivity
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.network.Data
import mezic.grega.hows_gregamezic.network.UserRegister
import mezic.grega.hows_gregamezic.network.UserRegisterResult
import mezic.grega.hows_gregamezic.network.SingletonApi
import mezic.grega.hows_gregamezic.utils.Util
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : MainBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // init shared prefs


        btn_register.isEnabled = false


        // setup the toolbar
        setupToolbar(getString(R.string.register_name))

        progressbar.visibility = View.GONE

        // button register listener
        btn_register.setOnClickListener {
            progressbar.visibility = View.VISIBLE
            if(validInputs()) {
                val email = input_email_registration.text.toString()
                val password = input_password_register.text.toString()
                SingletonApi.service.registerUser(UserRegister(email, password))
                    .enqueue(object : Callback<UserRegisterResult>{
                        override fun onFailure(call: Call<UserRegisterResult>, t: Throwable) {
                            progressbar.visibility = View.GONE
                            t.message?.let { toast(it) }
                            error(t)
                        }
                        override fun onResponse(call: Call<UserRegisterResult>, response: Response<UserRegisterResult>) {
                            progressbar.visibility = View.GONE
                            if (response.isSuccessful) {

                                // get the response data
                                val data: Data? = response.body()?.data

                                val email = data?.email

                                val intent : Intent = Intent(this@RegisterActivity, WelcomeActivity::class.java).apply {
                                    putExtra(Util.USERNAME_KEY, email)
                                }
                                startActivity(intent)
                                finish()
                            } else
                                toast("Error! Something went wrong, please try again")
                        }
                    })
            }
        }



        /*object: Callback<UserRegisterData>{
            override fun onFailure(call: Call<UserRegisterData>, t: Throwable) {
                toast(t.message.toString())
                error(t.message.toString())
            }

            override fun onResponse(call: Call<UserRegisterData>, response: Response<User>) {
                if (response.isSuccessful) {
                    startActivity(
                        Intent(
                            applicationContext,
                            WelcomeActivity::class.java
                        )
                    )
                    finish()
                }
            }
        })*/

        // set text watch listeners
        input_email_registration.addTextChangedListener(LoginTextWatcher())
        input_password_register.addTextChangedListener(LoginTextWatcher())
        input_password_register_again.addTextChangedListener(LoginTextWatcher())


    }


    private fun validInputs(): Boolean {
        return validateUsername() && validatePassword() && validatePasswordAgain()
    }

    private fun validateUsername(): Boolean {
        val username = input_email_registration.text.toString()
        if (username.trim().isEmpty()) {
            input_email_registration.error = "Username must contain at least one character!"
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            input_email_registration.error = "Enter correct email address!"
            return false
        }

        text_layout_email.error = null
        return true
    }

    private fun validatePassword(): Boolean {
        val password = input_password_register.text.toString()
        if (password.trim().length < 6) {
            input_layout_password_register.error = "Password must contain at least 6 character!"
            return false
        }
        input_layout_password_register.error = null
        return true
    }

    private fun validatePasswordAgain(): Boolean {
        val password = input_password_register.text.toString()
        val passwordAgain = input_password_register_again.text.toString()
        if (password != passwordAgain) {
            input_layout_password_register_again.error = "Passwords not match!"
            return false
        }
        input_layout_password_register_again.error = null
        return true
    }


    inner class LoginTextWatcher : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            btn_register.isEnabled = validInputs()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}
