package com.example.bulkemailapp.login

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.util.Log
import android.widget.Toast
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
    private var downloadResponse = MutableLiveData<Boolean>()

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

    internal val responseDownload: LiveData<Boolean>
        get() = downloadResponse

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

    internal fun hitDownloadCSV(link: String, context: Context) {
        repository.executeDownloadCsv(link, context, onDownloadComplete)
    }

    internal fun hitClearCSV() {
        downloadResponse = MutableLiveData()
    }

    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (Constants.downloadID == id) {
                val c: Cursor?
                val downloadManager: DownloadManager? =  context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val query: DownloadManager.Query? = DownloadManager.Query()

                if(query != null)
                {
                    query.setFilterByStatus(DownloadManager.STATUS_FAILED or DownloadManager.STATUS_PAUSED or DownloadManager.STATUS_SUCCESSFUL or
                            DownloadManager.STATUS_RUNNING or DownloadManager.STATUS_PENDING)
                }
                else
                    return

                c = downloadManager?.query(query)

                if(c!!.moveToFirst())
                {
                    when(c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                        DownloadManager.STATUS_PAUSED -> {
                        }
                        DownloadManager.STATUS_PENDING -> {
                        }
                        DownloadManager.STATUS_RUNNING -> {
                        }
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            downloadResponse.value = true
                        }
                        DownloadManager.STATUS_FAILED -> {
                            downloadResponse.value = false
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        disposable.clear()
    }
}