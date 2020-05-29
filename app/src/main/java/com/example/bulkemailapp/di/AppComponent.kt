package com.example.bulkemailapp.di

import com.example.bulkemailapp.addMoreEmail.AddEmailActivity
import com.example.bulkemailapp.data.Repository
import com.example.bulkemailapp.login.LoginActivity
import com.example.bulkemailapp.sendemail.SendEmail
import com.example.bulkemailapp.utils.Utils
import dagger.Component
import javax.inject.Singleton


@Component(modules = [AppModule::class, UtilsModule::class])
@Singleton
interface AppComponent {
    fun doInjection(repository: Repository)

    fun doInjection(sendEmail: SendEmail)

    fun doInjection(loginActivity: LoginActivity)

    fun doInjection(addEmailActivity: AddEmailActivity)

    fun doInjection(utils: Utils)
}