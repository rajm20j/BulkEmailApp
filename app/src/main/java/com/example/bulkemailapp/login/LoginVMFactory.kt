package com.example.bulkemailapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bulkemailapp.data.Repository
import com.example.bulkemailapp.utils.MailHelper
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class LoginVMFactory @Inject constructor(private val repository: Repository, private val mailHelper: MailHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository, mailHelper) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}