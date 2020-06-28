package com.example.bulkemailapp.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import com.example.bulkemailapp.extra.Constants
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun logout(context: Context) {
            SharedPrefHelper.clrStr()
            context.startActivity(Intent(context, LoginActivity::class.java))
            (context as AppCompatActivity).finishAffinity()
        }

        fun logoutFrag(context: FragmentActivity?) {
            SharedPrefHelper.clrStr()
            context?.startActivity(Intent(context, LoginActivity::class.java))
            (context as AppCompatActivity).finishAffinity()
        }

        fun popUpKeyboard(context: Context?, editText: EditText) {
            val imm: InputMethodManager? =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            editText.postDelayed({
                editText.requestFocus()
                imm!!.showSoftInput(editText, 0)
            }, 100)
        }

        fun hideKeyboard(context: Context?, view: View) {
            val imm: InputMethodManager? =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            if (view != null)
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun generateSnackbar(view: View, str: String) {
            val snack = Snackbar.make(view, str, Snackbar.LENGTH_INDEFINITE)
            ViewCompat.setElevation(snack.view, 10f)
            snack.show()
        }

        fun generateSnackbarShort(view: View, str: String) {
            val snack = Snackbar.make(view, str, Snackbar.LENGTH_LONG)
            ViewCompat.setElevation(snack.view, 10f)
            snack.show()
        }

        fun getCsvFromDocuments(context: Context): MutableList<List<String>> {
            val file =
                File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),Constants.filename)

            val inputStream = file.inputStream()

            val ids = mutableListOf<List<String>>()
            var id: List<String>
            val reader = BufferedReader(InputStreamReader(inputStream))
            try {
                var csv: String
                while (true) {
                    csv = reader.readLine()
                    id = csv.split(",")
                    ids.add(id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            SharedPrefHelper.setCSVList(ids)
            return ids
        }

        fun downloadCsv(
            link: String,
            context: Context,
            onDownloadComplete: BroadcastReceiver
        ) {
            Constants.filename = SimpleDateFormat("yyyymmddhhmmss").format(Calendar.getInstance().time).toString()+".csv"
            context.registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            val request =
                DownloadManager.Request(Uri.parse(link))
                    .setTitle("CSV downloading") // Title of the Download Notification
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE) // Visibility of the download Notification
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)
                    .setDestinationInExternalFilesDir(
                        context,
                        Environment.DIRECTORY_DOCUMENTS,
                        Constants.filename);

            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            Constants.downloadID =
                downloadManager.enqueue(request) // enqueue puts the download request in the queue
        }

        fun getId(url: String): String {
            if(url.contains("folderview"))
                return Constants.itIsAFolder
            else {
                val words = url.split('/')
                for (word in words)
                {
                    if(word.length == Constants.idLength)
                        return word
                }
            }
            return Constants.itIsAFolder
        }
    }
}