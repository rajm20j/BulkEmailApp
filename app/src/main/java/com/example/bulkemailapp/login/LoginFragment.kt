package com.example.bulkemailapp.login

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.R
import com.example.bulkemailapp.extra.Constants
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.sendemail.SendEmailFragment
import com.example.bulkemailapp.utils.MailHelper
import com.example.bulkemailapp.utils.Utils
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment(R.layout.fragment_login) {
    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    @Inject
    lateinit var loginVMFactory: LoginVMFactory

    @Inject
    lateinit var mailHelper: MailHelper

    lateinit var loginViewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as MyApp).myComponent.doInjection(this)

        (activity as AppCompatActivity).supportActionBar?.hide()

        loginViewModel =activity?.run {
            ViewModelProvider(this, loginVMFactory).get(LoginViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        loginViewModel.listResponse.observe(viewLifecycleOwner, Observer { this.consumeTestResponse(it) })

        btn_login.setOnClickListener {
            when {
                TextUtils.isEmpty(et_username.text) -> {
                    tv_username.isErrorEnabled = true
                    tv_password.errorIconDrawable = null
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tv_username.errorIconDrawable =
                            resources.getDrawable(R.drawable.ic_error, null)
                    } else {
                        tv_username.errorIconDrawable = resources.getDrawable(R.drawable.ic_error)
                    }
                    et_username.requestFocus()
                    Utils.popUpKeyboard(activity?.baseContext, et_username)
                }
                TextUtils.isEmpty(et_password.text) -> {
                    tv_username.errorIconDrawable = null
                    tv_password.isErrorEnabled = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        tv_password.errorIconDrawable =
                            resources.getDrawable(R.drawable.ic_error, null)
                    } else {
                        tv_password.errorIconDrawable = resources.getDrawable(R.drawable.ic_error)
                    }
                    et_password.requestFocus()
                    Utils.popUpKeyboard(activity?.baseContext, et_password)
                }
                else -> {
                    tv_password.errorIconDrawable = null
                    tv_username.errorIconDrawable = null
                    Utils.hideKeyboard(activity?.baseContext, btn_login)
                    mailHelper.setUser(
                        et_username.text.toString().trim(),
                        et_password.text.toString().trim()
                    )
                    loginViewModel.hitTestSession("smtp.gmail.com", "587")
                }
            }
        }
    }

    private fun consumeTestResponse(it: String?) {
        when (it) {
            Constants.loading -> {
                progress_loader.visibility = View.VISIBLE
            }
            Constants.True -> {
                progress_loader.visibility = View.GONE
                renderUpdateCompleteResponse()
            }
            Constants.False -> {
                progress_loader.visibility = View.GONE
                renderUpdateErrorResponse()
            }
            else -> Log.v("TAG", "Else mein aa gya :/")
        }
    }

    private fun renderUpdateErrorResponse() {
        mailHelper.removeUser()
//        Utils.popUpKeyboard(this, et_username)
//        Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_LONG).show()
        Utils.generateSnackbar(login_constraint, "Incorrect username or password")
    }

    private fun renderUpdateCompleteResponse() {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            replace(R.id.login_constraint, SendEmailFragment())
            commit()
        }
    }
}
