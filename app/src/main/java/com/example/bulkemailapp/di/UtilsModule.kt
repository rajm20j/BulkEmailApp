package com.example.bulkemailapp.di

import androidx.lifecycle.ViewModelProvider
import com.example.bulkemailapp.data.Repository
import com.example.bulkemailapp.login.LoginVMFactory
import com.example.bulkemailapp.utils.MailHelper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilsModule {
    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        val builder = GsonBuilder()
        return builder.setLenient().create()
    }

    @Provides
    @Singleton
    internal fun getRepository(): Repository {
        return Repository()
    }

    @Provides
    @Singleton
    internal fun getLoginVMFactory(repository: Repository, mailHelper: MailHelper): ViewModelProvider.Factory {
        return LoginVMFactory(repository, mailHelper)
    }

    @Provides
    @Singleton
    internal fun getMailHelper(): MailHelper {
        return MailHelper()
    }
}