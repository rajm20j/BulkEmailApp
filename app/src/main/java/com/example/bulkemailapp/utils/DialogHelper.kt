package com.example.bulkemailapp.utils

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.bulkemailapp.R
import com.example.bulkemailapp.bottomsheets.AddMoreEmailBottomSheet
import com.example.bulkemailapp.bottomsheets.EmailOptionsBottomSheet
import com.example.bulkemailapp.bottomsheets.HeadingListBottomSheet
import com.example.bulkemailapp.bottomsheets.TextFormattingBottomSheet
import com.example.bulkemailapp.bottomsheets.downloadLinkBottomList.DownloadLinkBottomSheet
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.custom_add_email_dialog.*

class DialogHelper {
    private lateinit var context: Context
    private lateinit var addEmailDialogListener: AddEmailDialogListener

    constructor(context: Context, addEmailDialogListener: AddEmailDialogListener)
    {
        this.context = context
        this.addEmailDialogListener = addEmailDialogListener
    }

    constructor()
    {

    }

    fun getAddEmailSlideUp(activity: FragmentActivity)
    {
        val addPhotoBottomDialogFragment = AddMoreEmailBottomSheet.newInstance()
        addPhotoBottomDialogFragment.show(
            activity.supportFragmentManager,
            "add_email_frag")

        activity.supportFragmentManager.executePendingTransactions()

        Utils.popUpKeyboard(context, addPhotoBottomDialogFragment.et_email_id)

        addPhotoBottomDialogFragment.btn_email.setOnClickListener {
            if (addPhotoBottomDialogFragment.et_email_id.text.toString().isNotEmpty()) {
                addPhotoBottomDialogFragment.dismiss()
                addEmailDialogListener.getEmailDialogBox(addPhotoBottomDialogFragment.et_email_id.text.toString().trim(), addPhotoBottomDialogFragment.et_name.text.toString().trim())
            } else {
                addPhotoBottomDialogFragment.et_email_id.error = context?.getString(R.string.email_cannot_empty)
            }
        }
    }

    fun getAddTextFormatterSlideUp(activity: FragmentActivity, editText: TextInputEditText?) {
        val textFormattingBottomSheet = TextFormattingBottomSheet.newInstance(editText)
        textFormattingBottomSheet.show(
            activity.supportFragmentManager,
            "text_formatter"
        )
    }

    fun getEmailOptionsSlideUp(activity: FragmentActivity) {
        val emailOptionsBS = EmailOptionsBottomSheet.newInstance()
        emailOptionsBS.show(
            activity.supportFragmentManager,
            "email_options"
        )
    }

    fun getHeadingListSlideUp(activity: FragmentActivity, listItem: List<String>) {
        val emailOptionsBS = HeadingListBottomSheet.newInstance(listItem)
        emailOptionsBS.show(
            activity.supportFragmentManager,
            "heading_list"
        )
    }

    fun getDownloadCsvSlideUp(activity: FragmentActivity) {
        val downloadBS = DownloadLinkBottomSheet.newInstance()
        downloadBS.show(
            activity.supportFragmentManager,
            "download_options"
        )
    }
}