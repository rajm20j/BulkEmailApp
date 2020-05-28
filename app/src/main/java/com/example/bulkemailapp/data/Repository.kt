package com.example.bulkemailapp.data

import android.content.Context
import android.util.Log
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.extra.Constants
import com.example.bulkemailapp.utils.MailHelper
import java.lang.Exception
import javax.inject.Inject
import javax.mail.*

class Repository {
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var mailHelper: MailHelper

    init {
        (MyApp.context as MyApp).myComponent.doInjection(this)
    }

    fun executeSendEmail(message: Message): String {
        return try {
            Transport.send(message)
            Constants.success
        } catch (e: Exception) {
            Constants.error = if (e.toString().length > 30) {
                e.toString().substring(0, 30)
            } else {
                e.toString()
            }
            Log.v("MAINN", e.toString())
            Constants.error
        }
    }

    fun testConn(host: String, port: String): String
    {
        return mailHelper.testConnection(host, port)
    }
}