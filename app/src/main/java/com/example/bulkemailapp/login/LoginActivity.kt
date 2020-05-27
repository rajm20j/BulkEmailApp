package com.example.bulkemailapp.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.R
import com.example.bulkemailapp.extra.Constants
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.sendemail.SendEmail
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        (application as MyApp).myComponent.doInjection(this)

        if (SharedPrefHelper.getStr(Constants.senderEmail) != "NULL") {
            startActivity(Intent(this, SendEmail::class.java))
            finishAffinity()
        }

        btn_login.setOnClickListener {
            when {
                TextUtils.isEmpty(et_username.text) -> {
                    tv_username.isErrorEnabled = true
                    tv_password.errorIconDrawable = null
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tv_username.errorIconDrawable = resources.getDrawable(R.drawable.ic_error, null)
                    }
                    else
                    {
                        tv_username.errorIconDrawable = resources.getDrawable(R.drawable.ic_error)
                    }
                    et_username.requestFocus()
                }
                TextUtils.isEmpty(et_password.text) -> {
                    tv_username.errorIconDrawable = null
                    tv_password.isErrorEnabled = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tv_password.errorIconDrawable = resources.getDrawable(R.drawable.ic_error, null)
                    }
                    else
                    {
                        tv_password.errorIconDrawable = resources.getDrawable(R.drawable.ic_error)
                    }
                    et_password.requestFocus()
                }
                else -> {
                    tv_password.errorIconDrawable = null
                    tv_username.errorIconDrawable = null
                    SharedPrefHelper.setStr(Constants.senderEmail, et_username?.text.toString().trim())
                    SharedPrefHelper.setStr(Constants.senderPass, et_password?.text.toString().trim())
                    Log.v("MAINN", SharedPrefHelper.getStr(Constants.senderEmail))
                    startActivity(Intent(this, SendEmail::class.java))
                    finishAffinity()
                }
            }
        }
    }
}
