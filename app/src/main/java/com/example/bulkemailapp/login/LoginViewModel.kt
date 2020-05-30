package com.example.bulkemailapp.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bulkemailapp.addMoreEmail.model.AddEmailListModel
import com.example.bulkemailapp.data.Repository
import com.example.bulkemailapp.extra.Constants
import com.example.bulkemailapp.utils.MailHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val repository: Repository, private val mailHelper: MailHelper) :
    ViewModel() {
    private val disposable = CompositeDisposable()
    private val responseLiveData = MutableLiveData<String>()
    private val hitSendResponse = MutableLiveData<String>()
    private val addEmailRvResponse = MutableLiveData<Boolean>()

    internal val listResponse: LiveData<String>
        get() = responseLiveData

    internal val responseHitSend: LiveData<String>
        get() = hitSendResponse

    internal val responseAddEmailRv: LiveData<Boolean>
        get() = addEmailRvResponse

    internal fun hitTestSession(host: String, port: String) {
        disposable.add(
            Observable.fromCallable {
                repository.testConn(host, port)
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

    internal fun hitSendMail(recipient: String, subject: String, body: String) {

        Log.v("MAINNN", "$recipient, $subject and $body")

        if (mailHelper.session == null)
            mailHelper.generateSession("smtp.gmail.com", "587")
        val message = mailHelper.generateMessage(recipient, subject, body)

        disposable.add(
            Observable.fromCallable {
                repository.executeSendEmail(message)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { hitSendResponse.value = Constants.loading }
                .subscribe(
                    { result -> hitSendResponse.value = result },
                    { error -> hitSendResponse.value = error.toString() }
                )
        )
    }

    internal fun addToRv(item: AddEmailListModel) {
        addEmailRvResponse.value = repository.addItem(item)
    }

    override fun onCleared() {
        disposable.clear()
    }
}