package com.example.bulkemailapp.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.extra.Constants
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.lang.Exception
import javax.inject.Inject
import javax.mail.*

class Repository {
    @Inject
    lateinit var context: Context

    init {
        (MyApp.context as MyApp).myComponent.doInjection(this)
    }

    fun executeSendEmail(message: Message): String {
        Log.v("MAINN", "Thread name: ${Thread.currentThread().name}")
        return try {
            Transport.send(message)
            Constants.success
        } catch (e: Exception) {
            Constants.error = if (e.toString().length > 30) {
                e.toString().substring(0, 30)
            } else {
                e.toString()
            }
            Constants.error
        }
    }
}