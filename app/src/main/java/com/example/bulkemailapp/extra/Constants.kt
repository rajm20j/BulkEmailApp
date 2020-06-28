package com.example.bulkemailapp.extra

import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.bulkemailapp.addMoreEmail.model.AddEmailListModel

object Constants {
    var downloadID: Long = 0
    const val senderEmail = "senderEmail"
    const val senderPass = "senderPass"
    const val loading = "loading"
    const val success = "success"
    var error = ""
    const val True = "true"
    const val False = "false"
    const val maxSpanStackSize = 20
    const val defaultSpinnerItem: String = "-- Select a variable --"
    const val idLength: Int = 33
    const val itIsAFolder = "itsAFolder"
    const val appendToDownloadURL = "https://drive.google.com/uc?export=download&id="
    var filename = "test.csv"
    var headingList: MutableList<String>? = mutableListOf()
    var arrayAdapter: ArrayAdapter<String>? = null
    var sendMailSpinner: Spinner? = null
}