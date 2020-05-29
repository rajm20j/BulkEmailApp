package com.example.bulkemailapp.sendemail

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.R
import com.example.bulkemailapp.addMoreEmail.AddEmailActivity
import com.example.bulkemailapp.extra.Constants
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.utils.Utils
import com.example.bulkemailapp.utils.parseHtml
import kotlinx.android.synthetic.main.activity_send_email.*
import javax.inject.Inject

class SendEmail : AppCompatActivity() {

    lateinit var sendEmailViewModel: SendEmailViewModel

    @Inject
    lateinit var sendEmailVMFactory: SendEmailVMFactory

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    var multipleMail = false
    var iterator = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_email)
        (application as MyApp).myComponent.doInjection(this)

        sendEmailViewModel =
            ViewModelProvider(this, sendEmailVMFactory).get(SendEmailViewModel::class.java)
        sendEmailViewModel.listResponse.observe(this, Observer { this.consumeUpdateResponse(it) })

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
                    Utils.popUpKeyboard(this, et_email)
                    et_email.setError(this.getString(R.string.need_recipient), null)
                }
                else
                {
                    Log.v("MAINNN", "Single")
                    sendEmailViewModel.hitSendMail(
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
                sendEmailViewModel.hitSendMail(
                    email,
                    et_subject.text.toString(),
                    msg
                )
                iterator += 1
            }
        }

        tv_email.setEndIconOnClickListener {
            startActivity(Intent(this, AddEmailActivity::class.java))
        }

        btn_add_more_to_list.setOnClickListener {
            startActivity(Intent(this, AddEmailActivity::class.java))
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
                Toast.makeText(this, "Authentication error, please login again", Toast.LENGTH_LONG)
                    .show()
                Utils.logout(this)
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
                sendEmailViewModel.hitSendMail(
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
                sendEmailViewModel.hitSendMail(
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.logout_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            Utils.logout(this)
            return true
        }
        if (item.itemId == R.id.dark_switch) {
            if (item.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            item.isChecked = !item.isChecked
        }
        return super.onOptionsItemSelected(item)
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
