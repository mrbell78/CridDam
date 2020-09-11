package com.criddam.medicine.registration.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.criddam.medicine.R
import com.criddam.medicine.others.Utility
import com.criddam.medicine.registration.viewmodels.RegistrationViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModelProvider: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        action()
    }

    private fun action() {
        viewModelProvider =
            androidx.lifecycle.ViewModelProviders.of(this).get(RegistrationViewModel::class.java)

        tv_already_memeber.setOnClickListener {
            finish()
        }

        btn_registration_submit.setOnClickListener {
            when {
                et_reg_first_name.text.toString() == "" -> et_reg_first_name.error =
                    getString(R.string.enter_first_name)
                et_reg_last_name.text.toString() == "" -> et_reg_last_name.error =
                    getString(R.string.enter_last_name)
                et_reg_phone_num.text.toString() == "" -> et_reg_phone_num.error =
                    getString(R.string.enter_mobile_number)
                !Utility.isValidMobile(et_reg_phone_num.text.toString()) ->
                    et_reg_phone_num.error = getString(R.string.invalid_number)
                et_reg_password.text.toString() == "" -> et_reg_password.error =
                    getString(R.string.enter_password)
                et_reg_password.text.toString().length < 6 ->
                    Toast.makeText(
                        this,
                        getString(R.string.enter_six_digit_password),
                        Toast.LENGTH_LONG
                    ).show()
                else -> {
                    if (Utility.verifyAvailableNetwork(this)) {
                        viewModelProvider.registerWithNumberAndPass(
                            this,
                            et_reg_first_name.text.toString(),
                            et_reg_last_name.text.toString(),
                            et_reg_phone_num.text.toString(),
                            et_reg_password.text.toString(),
                            "app_user"
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
                Toast.makeText(this, getString(R.string.registration_successful), Toast.LENGTH_LONG)
                    .show()
                finish()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.registration_failed_try_with_another_number),
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
