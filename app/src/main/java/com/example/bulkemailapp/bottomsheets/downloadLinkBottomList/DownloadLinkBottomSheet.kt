package com.example.bulkemailapp.bottomsheets.downloadLinkBottomList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.lifecycle.ViewModelProvider
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.R
import com.example.bulkemailapp.bottomsheets.AddMoreEmailBottomSheet
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.login.LoginVMFactory
import com.example.bulkemailapp.login.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class DownloadLinkBottomSheet : BottomSheetDialogFragment() {
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var loginVMFactory: LoginVMFactory

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as MyApp).myComponent.doInjection(this)

        loginViewModel = activity?.run {
            ViewModelProvider(this, loginVMFactory).get(LoginViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(
            R.layout.fragment_download_sheet
            , container,
            false
        )
    }

    companion object {
        fun newInstance(): DownloadLinkBottomSheet {
            return DownloadLinkBottomSheet()
        }
    }
}