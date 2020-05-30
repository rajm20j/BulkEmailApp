package com.example.bulkemailapp.sendemail

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.R
import com.example.bulkemailapp.addMoreEmail.AddEmailFragment
import com.example.bulkemailapp.extra.Constants
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.login.LoginVMFactory
import com.example.bulkemailapp.login.LoginViewModel
import com.example.bulkemailapp.utils.Utils
import com.example.bulkemailapp.utils.parseHtml
import kotlinx.android.synthetic.main.fragment_send_email.*
import javax.inject.Inject

class SendEmailFragment : Fragment(R.layout.fragment_send_email) {
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var loginVMFactory: LoginVMFactory

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    var multipleMail = false
    var iterator = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as MyApp).myComponent.doInjection(this)

        (activity as AppCompatActivity).supportActionBar?.show()

        loginViewModel =activity?.run {
            ViewModelProvider(this, loginVMFactory).get(LoginViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        loginViewModel.responseHitSend.observe(viewLifecycleOwner, Observer { this.consumeUpdateResponse(it) })

        initializeClickListeners()
//        enableTextFormat()
    }

    private fun enableTextFormat() {
        val str = "<u><i><b>Message</b></i></u>"
        et_msg.setText(str.parseHtml())
    }

    private fun initializeClickListeners() {
        btn_send.setOnClickListener {
            Log.v("MAINNN", multipleMail.toString())
            if (sharedPrefHelper.getEmailList().isEmpty()) {
                if (TextUtils.isEmpty(et_email.text)) {
                    et_email.requestFocus()
                    Utils.popUpKeyboard(activity?.baseContext, et_email)
                    et_email.setError(this.getString(R.string.need_recipient), null)
                }
                else
                {
                    Log.v("MAINNN", "Single")
                    loginViewModel.hitSendMail(
                        et_email.text.toString().trim(),
                        et_subject.text.toString(),
                        et_msg.text.toString()
                    )
                }
            } else {
                Log.v("MAINNN", "Multiple")
                multipleMail = true
                val email = sharedPrefHelper.getEmailList()[iterator].email
                val msg = sharedPrefHelper.getEmailList()[iterator].name
                loginViewModel.hitSendMail(
                    email,
                    et_subject.text.toString(),
                    msg
                )
                iterator += 1
            }
        }

        tv_email.setEndIconOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                replace(R.id.login_constraint, AddEmailFragment())
                addToBackStack(null)
                commit()
            }
        }

        btn_add_more_to_list.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                replace(R.id.login_constraint, AddEmailFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun consumeUpdateResponse(msg: String) {
        when (msg) {
            Constants.loading -> {
            }
            Constants.success -> {
                renderUpdateCompleteResponse()
            }
            Constants.error -> {
                renderUpdateErrorResponse()
            }
            else -> Log.v("TAG", "Else mein aa gya :/")
        }
    }

    private fun renderUpdateErrorResponse() {
        Log.v("MAINN", Constants.error)
        when (Constants.error) {
            "javax.mail.AuthenticationFaile" -> {
                Toast.makeText(activity?.baseContext, "Authentication error, please login again", Toast.LENGTH_LONG)
                    .show()
                Utils.logoutFrag(activity)
            }
            "javax.mail.SendFailedException" -> {
                Utils.generateSnackbar(send_email_coord, "Network Error")
            }
            else -> {
                Utils.generateSnackbar(send_email_coord, "Some error occurred")
            }
        }

        if (multipleMail) {
            if (iterator != sharedPrefHelper.getEmailList().size) {
                val email = sharedPrefHelper.getEmailList()[iterator].email
                val msg = sharedPrefHelper.getEmailList()[iterator].name
                loginViewModel.hitSendMail(
                    email,
                    et_subject.text.toString(),
                    msg
                )
                iterator += 1
            } else {
                multipleMail = false
                Utils.generateSnackbar(send_email_coord, "Unable to send ${iterator}th mails")
            }
        }
    }

    private fun renderUpdateCompleteResponse() {
        if (multipleMail) {
            if (iterator != sharedPrefHelper.getEmailList().size) {
                val email = sharedPrefHelper.getEmailList()[iterator].email
                val msg = sharedPrefHelper.getEmailList()[iterator].name
                loginViewModel.hitSendMail(
                    email,
                    et_subject.text.toString(),
                    msg
                )
                iterator += 1
            } else {
                multipleMail = false
                Utils.generateSnackbar(send_email_coord, "Sent all the mails")
                return
            }
        } else
            Utils.generateSnackbarShort(send_email_coord, "Email Sent")
    }

    override fun onResume() {
        super.onResume()
        iterator = 0
        if (sharedPrefHelper.getEmailList().isNotEmpty()) {
            btn_add_more_to_list.visibility = View.VISIBLE
            tv_email.visibility = View.INVISIBLE
        }
    }
}
