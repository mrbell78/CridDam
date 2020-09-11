package com.criddam.medicine.forgetPassword.views

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.criddam.medicine.R
import com.criddam.medicine.forgetPassword.viewModel.ForgotPasswordViewModel
import com.criddam.medicine.homePage.HomeActivity
import com.criddam.medicine.others.Utility
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var viewModelProvider: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        action()
    }

    private fun action() {
        viewModelProvider =
            androidx.lifecycle.ViewModelProviders.of(this).get(ForgotPasswordViewModel::class.java)
        btn_update_password_submit.setOnClickListener {
            when {
                et_forgot_pass_mobile_num.text.toString() == "" -> et_forgot_pass_mobile_num.error = getString(R.string.mobile_number)

                et_forgot_pass_new_password.text.toString() == "" -> Toast.makeText(this, getString(R.string.enter_password), Toast.LENGTH_LONG).show()

                et_forgot_pass_new_password.text.toString().length < 6 -> Toast.makeText(
                    this,
                    getString(R.string.enter_six_digit_password),
                    Toast.LENGTH_LONG
                ).show()

                et_forgot_pass_confirm_password.text.toString() == "" -> Toast.makeText(this, getString(R.string.enter_password), Toast.LENGTH_LONG).show()

                et_forgot_pass_new_password.text.toString() != et_forgot_pass_confirm_password.text.toString() -> Toast.makeText(
                    this,
                    getString(R.string.password_mismatch),
                    Toast.LENGTH_LONG
                ).show()

                else -> {
                    if (Utility.verifyAvailableNetwork(this)) {
                        viewModelProvider.updatingPassword(
                            this,
                            et_forgot_pass_mobile_num.text.toString(),
                            et_forgot_pass_new_password.text.toString()
                        )
                    } else Toast.makeText(
                        this,
                        getString(R.string.no_internet_connection),
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
        }

        viewModelProvider.isSuccess.observe(this, Observer {
            if (it) {
                Toast.makeText(
                    this,
                    getString(R.string.password_updating_successful),
                    Toast.LENGTH_LONG
                )
                    .show()
                finish()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.password_updating_failed),
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
