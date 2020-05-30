package com.example.bulkemailapp.utils

import android.app.AlertDialog
import android.content.Context
import android.view.View
import com.example.bulkemailapp.R
import kotlinx.android.synthetic.main.custom_add_email_dialog.view.*

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