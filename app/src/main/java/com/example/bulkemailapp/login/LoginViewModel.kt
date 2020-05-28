package com.example.bulkemailapp.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bulkemailapp.data.Repository
import com.example.bulkemailapp.extra.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel (private val repository: Repository) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val responseLiveData = MutableLiveData<String>()

    internal val listResponse: LiveData<String>
        get() = responseLiveData

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

    override fun onCleared() {
        disposable.clear()
    }
}