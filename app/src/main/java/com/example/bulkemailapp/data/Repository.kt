package com.example.bulkemailapp.data

import android.util.Log
import com.example.bulkemailapp.MyApp
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.lang.Exception
import javax.mail.*

class Repository {
    init {
        (MyApp.context as MyApp).myComponent.doInjection(this)
    }
    fun executeSendEmail(message: Message): Completable {
        Log.v("MAINN", "Thread name: ${Thread.currentThread().name}")
        return try {
            Transport.send(message)
            Completable.complete()
        } catch (e: Exception)
        {
            Log.v("MAINN", e.toString())
            Completable.error(e)
        }
    }
}