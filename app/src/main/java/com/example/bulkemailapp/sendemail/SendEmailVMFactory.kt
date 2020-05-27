package com.example.bulkemailapp.sendemail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bulkemailapp.data.Repository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class SendEmailVMFactory @Inject constructor(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SendEmailViewModel::class.java)) {
            return SendEmailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}