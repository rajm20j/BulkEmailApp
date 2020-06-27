package com.example.bulkemailapp.bottomsheets.downloadLinkBottomList

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.R
import com.example.bulkemailapp.bottomsheets.AddMoreEmailBottomSheet
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.login.LoginVMFactory
import com.example.bulkemailapp.login.LoginViewModel
import com.example.bulkemailapp.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_download_sheet.*
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

    private fun consumeDownloadResponse(it: Boolean) {
        if(it)
        {
            Toast.makeText(activity?.baseContext, "Download Complete", Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(activity?.baseContext, "Error Downloading", Toast.LENGTH_LONG).show()
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.responseDownload.observe(
            viewLifecycleOwner,
            Observer { consumeDownloadResponse(it) })

        btn_download.setOnClickListener {
            if(!TextUtils.isEmpty(et_download.text))
                loginViewModel.hitDownloadCSV(et_download.text.toString().trim(), activity!!.baseContext)
        }
    }

    companion object {
        fun newInstance(): DownloadLinkBottomSheet {
            return DownloadLinkBottomSheet()
        }
    }
}