package com.example.bulkemailapp.sendemail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bulkemailapp.data.Repository
import com.example.bulkemailapp.extra.Constants
import com.example.bulkemailapp.extra.SharedPrefHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SendEmailViewModel(private val repository: Repository) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val responseLiveData = MutableLiveData<String>()

    internal val listResponse: LiveData<String>
        get() = responseLiveData

    internal fun hitSendMail(to: String, subject: String, msg: String) {

        Log.v("MAINNN", "$to, $subject and $msg")

        val yourEmail = SharedPrefHelper.getStr(Constants.senderEmail)
        val yourPassword = SharedPrefHelper.getStr(Constants.senderPass)

        val properties = Properties()
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.port"] = "587"

        Log.v("MAINN", "properties")

        val session: Session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                Log.v("MAINN", "getPasswordAuthentication()")
                return PasswordAuthentication(yourEmail, yourPassword)
            }
        })
        val message: Message = MimeMessage(session)
        try {
            Log.v("MAINN", "SendMail")
            message.setFrom(InternetAddress(yourEmail))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to.trim()))
            message.subject = subject.trim()
            message.setText(msg.trim())
        } catch (e: Exception) {
            e.printStackTrace()
            Log.v("MAINN", e.toString())
        }

        disposable.add(
            Observable.fromCallable {
                repository.executeSendEmail(message)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { responseLiveData.value = Constants.loading }
                .subscribe(
                    { result -> responseLiveData.value = result },
                    { error -> responseLiveData.value = error.toString() }
                )
        )
    }

    override fun onCleared() {
        disposable.clear()
    }
}