package com.example.bulkemailapp.addMoreEmail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bulkemailapp.addMoreEmail.model.AddEmailListModel
import com.example.bulkemailapp.data.Repository
import com.example.bulkemailapp.extra.Constants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddEmailViewModel (private val repository: Repository) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val responseLiveData = MutableLiveData<Boolean>()

    internal val listResponse: LiveData<Boolean>
        get() = responseLiveData

    internal fun addToRv(item: AddEmailListModel) {
        responseLiveData.value = repository.addItem(item)
    }

    override fun onCleared() {
        disposable.clear()
    }
}