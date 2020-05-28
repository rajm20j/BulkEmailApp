package com.example.bulkemailapp.sendemail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bulkemailapp.data.Repository
import com.example.bulkemailapp.extra.Constants
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.utils.MailHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SendEmailViewModel(private val repository: Repository, private val mailHelper: MailHelper) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val responseLiveData = MutableLiveData<String>()

    internal val listResponse: LiveData<String>
        get() = responseLiveData

    internal fun hitSendMail(recipient: String, subject: String, body: String) {

        Log.v("MAINNN", "$recipient, $subject and $body")

        if(mailHelper.session == null)
            mailHelper.generateSession("smtp.gmail.com", "587")
        val message = mailHelper.generateMessage(recipient, subject, body)

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