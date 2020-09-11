package com.criddam.medicine.login.views

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.criddam.medicine.R
import com.criddam.medicine.database.OfflineInformation
import com.criddam.medicine.forgetPassword.views.ForgetPasswordActivity
import com.criddam.medicine.homePage.HomeActivity
import com.criddam.medicine.login.viewmodels.LoginViewModel
import com.criddam.medicine.others.LocaleHelper
import com.criddam.medicine.others.Utility
import com.criddam.medicine.registration.views.RegisterActivity
import com.criddam.medicine.rubelportion.HealthStatus
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModelProvider: LoginViewModel
    private lateinit var offlineInformation: OfflineInformation
    private var notificationType: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Changing language to bangla
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            val locale = Locale("bn")
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
        } else {
            LocaleHelper.setLocale(this, "bn")
        }

        setContentView(R.layout.activity_login)
        action()
    }

    private fun action() {

        val intent = intent
        if (intent.hasExtra("notification_type")) {
            notificationType = intent.getStringExtra("notification_type")
            Log.e("LoginAc", notificationType)
        }

        offlineInformation = OfflineInformation(this)
        if (offlineInformation.token != "") {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("notification", notificationType)
            startActivity(intent)
            finish()
        }
        viewModelProvider =
            androidx.lifecycle.ViewModelProviders.of(this).get(LoginViewModel::class.java)

        tv_signup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        tv_forgot_pass.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        btn_login_submit.setOnClickListener {
            if (et_login_mobile_num.text.toString() == "")
                et_login_mobile_num.error = getString(R.string.enter_mobile_number)
            else if (!Utility.isValidMobile(et_login_mobile_num.text.toString()))
                et_login_mobile_num.error = getString(R.string.invalid_number)
            else if (et_login_password.text.toString() == "")
                Toast.makeText(
                    this,
                    getString(R.string.enter_password),
                    Toast.LENGTH_LONG
                ).show()
            else {
                if (Utility.verifyAvailableNetwork(this)) {

                   /* HealthStatus.userid(et_login_mobile_num.text.toString().trim(), et_login_password.text.toString().trim())*/
                    viewModelProvider.loginWithNumberAndPass(
                        this,
                        et_login_mobile_num.text.toString().trim(),
                        et_login_password.text.toString().trim()

                    )
                } else Toast.makeText(
                    this,
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModelProvider.isSuccess.observe(this, Observer {
            if (it) {
                Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_LONG)
                    .show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.login_failed_try_with_another_number),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
