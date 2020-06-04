package com.example.bulkemailapp.login

import android.content.Context
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
//    private val fetchOffListResponse = MutableLiveData<String>()
    private val fetchOffListResponse = MutableLiveData<MutableList<List<String>>>()

    internal val listResponse: LiveData<String>
        get() = responseLiveData

    internal val responseHitSend: LiveData<String>
        get() = hitSendResponse

    internal val responseAddEmailRv: LiveData<Boolean>
        get() = addEmailRvResponse

    /*internal val responseOffListResponse: LiveData<String>
        get() = fetchOffListResponse*/

    internal val responseOffListResponse: LiveData<MutableList<List<String>>>
        get() = fetchOffListResponse

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

    /*internal fun hitFetchOfflineApi(context: Context) {
        repository.executeGetData(context)
        disposable.add(repository.executeGetData(context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { fetchOffListResponse.value = "loading" }
            .subscribe(
                { result -> fetchOffListResponse.value = result },
                { error -> fetchOffListResponse.value = "error" }
            ))
    }*/

    internal fun hitFetchOfflineApi(context: Context) {
//        repository.executeGetCsvData(context)
        disposable.add(repository.executeGetCsvData(context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> fetchOffListResponse.value = result },
                { fetchOffListResponse.value = mutableListOf() }
            ))
    }

    override fun onCleared() {
        disposable.clear()
    }
}