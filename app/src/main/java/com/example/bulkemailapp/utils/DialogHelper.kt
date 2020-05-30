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

    fun getAddEmailDialog()
    {
        val enterEmailDialog = AlertDialog.Builder(context).create()

        val view = View.inflate(context, R.layout.custom_add_email_dialog, null)

        Utils.popUpKeyboard(context, view.et_email_id)

        view.btn_email.setOnClickListener {
            if (view.et_email_id.text.toString().isNotEmpty()) {
                enterEmailDialog.dismiss()
                addEmailDialogListener.getEmailDialogBox(view.et_email_id.text.toString().trim(), view.et_name.text.toString().trim())
            } else {
                view.et_email_id.error = context?.getString(R.string.email_cannot_empty)
            }
        }
        enterEmailDialog.setView(view)
        enterEmailDialog.show()

        enterEmailDialog.setOnDismissListener { addEmailDialogListener.dismissDialog() }
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

    fun getEditEmailDialog()
    {
        val enterEmailDialog = AlertDialog.Builder(context).create()

        val view = View.inflate(context, R.layout.custom_add_email_dialog, null)

        Utils.popUpKeyboard(context, view.et_email_id)

        view.btn_email.setOnClickListener {
            if (view.et_email_id.text!!.isValidEmail()) {
                enterEmailDialog.dismiss()
                addEmailDialogListener.getEmailDialogBox(view.et_email_id.text.toString().trim(), view.et_name.text.toString().trim())
            } else {
                view.et_email_id.error = context?.getString(R.string.need_recipient)
            }
        }
        enterEmailDialog.setView(view)
        enterEmailDialog.show()

        enterEmailDialog.setOnDismissListener { addEmailDialogListener.dismissDialog() }
    }
}