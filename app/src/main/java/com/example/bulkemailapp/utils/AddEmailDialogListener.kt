package com.example.bulkemailapp.utils

interface AddEmailDialogListener {
    fun getEmailDialogBox(email: String, name: String)
    fun dismissDialog()
}