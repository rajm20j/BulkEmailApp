package com.example.bulkemailapp.utils

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.login.LoginActivity

class Utils {
    companion object {
        fun logout(context: Context)
        {
            SharedPrefHelper.clrStr()
            context.startActivity(Intent(context, LoginActivity::class.java))
            (context as AppCompatActivity).finishAffinity()
        }

        fun popUpKeyboard(context: Context, editText: EditText) {
            val imm: InputMethodManager? = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            editText.postDelayed({
                editText.requestFocus()
                imm!!.showSoftInput(editText, 0)
            }, 100)
        }

        fun hideKeyboard(context: Context, view: View) {
            val imm: InputMethodManager? = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            if(view!=null)
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}