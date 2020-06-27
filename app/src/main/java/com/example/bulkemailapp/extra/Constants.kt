package com.example.bulkemailapp.extra

import com.example.bulkemailapp.addMoreEmail.model.AddEmailListModel

object Constants {
    var downloadID: Long = 0
    var isDownloaded = false
    var downloadURL: String = "https://drive.google.com/uc?export=download&id=1Aob_eQ5jlEeoa60GRNFMeZZjBJ78f0vj"
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
}