package com.example.bulkemailapp.bottomsheets

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.R
import com.example.bulkemailapp.extra.Constants
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.login.LoginVMFactory
import com.example.bulkemailapp.login.LoginViewModel
import com.example.bulkemailapp.utils.DialogHelper
import com.example.bulkemailapp.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_download_sheet.*
import javax.inject.Inject

class DownloadLinkBottomSheet : BottomSheetDialogFragment() {
    private lateinit var loginViewModel: LoginViewModel

    private var READ_PERMISSION_CODE = 1

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
        Log.v("MAINN", "Isme ja ra hai")
        if(it)
        {
            Toast.makeText(activity?.baseContext, "Download Complete", Toast.LENGTH_LONG).show()

            val list = Utils.getCsvFromDocuments(activity!!.baseContext)

            val headingList = mutableListOf<String>()
            for (item in list[0])
                headingList.add(item)

            Constants.headingList?.clear()
            Constants.headingList?.add(Constants.defaultSpinnerItem)
            Constants.headingList?.addAll(headingList)
            Constants.arrayAdapter?.notifyDataSetChanged()
            Constants.sendMailSpinner?.visibility = View.VISIBLE

            val dialogHelper = DialogHelper()
            dialogHelper.getHeadingListSlideUp(activity!!, headingList)
            dismiss()
            loginViewModel.hitClearCSV()
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
            if(ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                download()
            else
                requestPermissions()
        }
    }

    private fun requestPermissions() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)){
            AlertDialog.Builder(activity!!)
                .setTitle("Permission Needed")
                .setMessage("This permission is needed to access the downloaded csv file.")
                .setPositiveButton("Ok") { _, _ ->
                    requestPermissions(listOf(Manifest.permission.READ_EXTERNAL_STORAGE).toTypedArray(), READ_PERMISSION_CODE)
                }
                .setNegativeButton("Cancel") {dialog, _ ->
                    dialog.dismiss()
                }.create().show()
        }
        else{
            requestPermissions(listOf(Manifest.permission.READ_EXTERNAL_STORAGE).toTypedArray(), READ_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == READ_PERMISSION_CODE)
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                download()
            else
                Toast.makeText(activity!!.baseContext, "Permission DENIED", Toast.LENGTH_SHORT).show()
    }

    private fun download() {
        if(!TextUtils.isEmpty(et_download.text)) {
            val id = Utils.getId(et_download.text.toString().trim())
            if (id == Constants.itIsAFolder)
                Toast.makeText(activity!!.baseContext, "Need direct link of csv file, not of the folder", Toast.LENGTH_LONG).show()
            else
                loginViewModel.hitDownloadCSV(Constants.appendToDownloadURL+id, activity!!.baseContext)
        }
    }

    companion object {
        fun newInstance(): DownloadLinkBottomSheet {
            return DownloadLinkBottomSheet()
        }
    }
}