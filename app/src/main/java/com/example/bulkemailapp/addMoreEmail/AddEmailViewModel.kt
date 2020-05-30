package com.example.bulkemailapp.addMoreEmail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bulkemailapp.addMoreEmail.model.AddEmailListModel
import com.example.bulkemailapp.data.Repository
import io.reactivex.disposables.CompositeDisposable

class AddEmailViewModel (private val repository: Repository) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val addEmailRvResponse = MutableLiveData<Boolean>()

    internal val responseAddEmailRv: LiveData<Boolean>
        get() = addEmailRvResponse

    internal fun addToRv(item: AddEmailListModel) {
        addEmailRvResponse.value = repository.addItem(item)
    }

    override fun onCleared() {
        disposable.clear()
    }
}