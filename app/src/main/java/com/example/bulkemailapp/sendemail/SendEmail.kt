package com.example.bulkemailapp.sendemail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.R
import com.example.bulkemailapp.data.model.ApiResponse
import com.example.bulkemailapp.data.model.Status
import com.example.bulkemailapp.extra.Constants
import com.example.bulkemailapp.utils.Utils
import kotlinx.android.synthetic.main.activity_send_email.*
import javax.inject.Inject

class SendEmail : AppCompatActivity() {

    lateinit var sendEmailViewModel: SendEmailViewModel

    @Inject
    lateinit var sendEmailVMFactory: SendEmailVMFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_email)
        (application as MyApp).myComponent.doInjection(this)

        sendEmailViewModel =
            ViewModelProvider(this, sendEmailVMFactory).get(SendEmailViewModel::class.java)
        sendEmailViewModel.listResponse.observe(this, Observer { this.consumeUpdateResponse(it) })

        btn_send.setOnClickListener {
            if(TextUtils.isEmpty(et_email.text))
            {
                et_email.requestFocus()
                Utils.popUpKeyboard(this, et_email)
                Toast.makeText(this, "Need a recipient address", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            sendEmailViewModel.hitSendMail(
                et_email.text.toString(),
                et_subject.text.toString(),
                et_msg.text.toString()
            )
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
        if(Constants.error.equals("javax.mail.AuthenticationFaile"))
        {
            Toast.makeText(this, "Authentication error, please login again", Toast.LENGTH_LONG).show()
            Utils.logout(this)
        }
        else if(Constants.error.equals("javax.mail.SendFailedException"))
        {
            Toast.makeText(this, "Network Error", Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_LONG).show()
        }
    }

    private fun renderUpdateCompleteResponse() {
        Toast.makeText(this, "Email Sent", Toast.LENGTH_LONG).show()
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
        if (item.itemId == R.id.dark_switch)
        {
            if(item.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            item.isChecked = !item.isChecked
        }
        return super.onOptionsItemSelected(item)
    }
}
