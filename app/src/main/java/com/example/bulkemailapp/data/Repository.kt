package com.example.bulkemailapp.data

import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.addMoreEmail.model.AddEmailListModel
import com.example.bulkemailapp.extra.Constants
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.utils.MailHelper
import com.example.bulkemailapp.utils.Utils
import javax.inject.Inject
import javax.mail.Message
import javax.mail.Transport

class Repository {
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var mailHelper: MailHelper

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

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

    fun addItem(item: AddEmailListModel): Boolean {
        return sharedPrefHelper.addToList(item)
    }

    fun executeDownloadCsv(
        link: String,
        context: Context,
        onDownloadComplete: BroadcastReceiver
    ) {
        Utils.downloadCsv(link, context, onDownloadComplete)
    }
}