package tradeapp.ateneo.edu.tradeapp

import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_login.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import tradeapp.ateneo.edu.tradeapp.service.AuthenticationException
import tradeapp.ateneo.edu.tradeapp.service.UserService

@EActivity(R.layout.activity_login)
open class LoginActivity : AppCompatActivity(){

    @Bean
    lateinit var userService: UserService

    @AfterViews
    open protected fun afterSetup(){
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin() }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {

        // Reset errors.
        usernameText.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val usernameStr = usernameText.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(usernameStr)) {
            usernameText.error = getString(R.string.error_field_required)
            focusView = usernameText
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            try {
                userService.loginOrCreate(usernameStr, passwordStr)
                finish()
            } catch(e: AuthenticationException){
                usernameText.error = e.message
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }
}
