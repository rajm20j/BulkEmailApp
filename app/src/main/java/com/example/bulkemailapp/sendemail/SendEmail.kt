package com.example.bulkemailapp.sendemail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.R
import com.example.bulkemailapp.data.model.ApiResponse
import com.example.bulkemailapp.data.model.Status
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

        sendEmailViewModel =ViewModelProvider(this, sendEmailVMFactory).get(SendEmailViewModel::class.java)
        sendEmailViewModel.listResponse.observe(this, Observer<ApiResponse> { this.consumeUpdateResponse(it) })

        btn_send.setOnClickListener {
            Toast.makeText(this, "Toast", Toast.LENGTH_SHORT).show()
            sendEmailViewModel.hitSendMail(et_email.text.toString(), et_subject.text.toString(), et_msg.text.toString())
        }
    }

    private fun consumeUpdateResponse(apiResponse: ApiResponse) {
        when (apiResponse.status) {
            Status.LOADING ->{}
            Status.COMPLETE -> {
                renderUpdateCompleteResponse()
            }
            Status.ERROR -> {
                renderUpdateErrorResponse(apiResponse.error)
            }
            else -> Log.v("TAG", "Else mein aa gya :/")
        }
    }

    private fun renderUpdateErrorResponse(error: Throwable?) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun renderUpdateCompleteResponse() {
        Toast.makeText(this, "Sent", Toast.LENGTH_LONG).show()
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
        return super.onOptionsItemSelected(item)
    }
}
