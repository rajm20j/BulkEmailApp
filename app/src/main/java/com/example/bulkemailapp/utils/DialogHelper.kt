package com.example.bulkemailapp.utils

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.bulkemailapp.R
import com.example.bulkemailapp.bottomsheets.AddMoreEmailBottomSheet
import kotlinx.android.synthetic.main.custom_add_email_dialog.*
import kotlinx.android.synthetic.main.custom_add_email_dialog.view.*
import kotlinx.android.synthetic.main.custom_add_email_dialog.view.et_email_id

class DialogHelper {
    private var context: Context?
    private var addEmailDialogListener: AddEmailDialogListener

    constructor(context: Context?, addEmailDialogListener: AddEmailDialogListener)
    {
        this.context = context
        this.addEmailDialogListener = addEmailDialogListener
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
}