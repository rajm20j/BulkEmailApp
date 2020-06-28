package com.example.bulkemailapp.di

import com.example.bulkemailapp.addMoreEmail.AddEmailFragment
import com.example.bulkemailapp.bottomsheets.TextFormattingBottomSheet
import com.example.bulkemailapp.bottomsheets.DownloadLinkBottomSheet
import com.example.bulkemailapp.data.Repository
import com.example.bulkemailapp.login.LoginActivity
import com.example.bulkemailapp.login.LoginFragment
import com.example.bulkemailapp.sendemail.SendEmailFragment
import com.example.bulkemailapp.utils.Utils
import dagger.Component
import javax.inject.Singleton


@Component(modules = [AppModule::class, UtilsModule::class])
@Singleton
interface AppComponent {
    fun doInjection(repository: Repository)

    fun doInjection(utils: Utils)

    fun doInjection(sendEmailFragment: SendEmailFragment)

    fun doInjection(loginActivity: LoginActivity)

    fun doInjection(loginFragment: LoginFragment)

    fun doInjection(addEmailFragment: AddEmailFragment)

    fun doInjection(textFormattingBottomSheet: TextFormattingBottomSheet)

    fun doInjection(downloadLinkBottomSheet: DownloadLinkBottomSheet)
}