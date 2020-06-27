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
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import com.example.bulkemailapp.R
import com.example.bulkemailapp.extra.Constants
import java.io.BufferedReader
import java.io.InputStreamReader

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

        fun getJson(context: Context): String? {
            val jsonString: String
            try {
                val inStream = context.resources.openRawResource(R.raw.data)
                val buffer = ByteArray(inStream.available())
                inStream.use { it.read(buffer) }

                jsonString = String(buffer)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
            return jsonString
        }

        fun getCsv(context: Context): MutableList<List<String>> {
            val inputStream = context.resources.openRawResource(R.raw.data_1)

            val ids = mutableListOf<List<String>>()
            var id = listOf<String>()

            val reader = BufferedReader(InputStreamReader(inputStream))
            try {
                var csv = ""
                while (true) {
                    csv = reader.readLine()
                    id = csv.split(",")
                    ids.add(id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ids
        }

        fun downloadCsv(
            link: String,
            context: Context,
            onDownloadComplete: BroadcastReceiver
        ) {
            context.registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            val request =
                DownloadManager.Request(Uri.parse(link))
                    .setTitle("Dummy File") // Title of the Download Notification
                    .setDescription("Downloading") // Description of the Download Notification
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE) // Visibility of the download Notification
//                    .setDestinationUri(Uri.fromFile(file)) // Uri of the destination file
//                    .setRequiresCharging(false) // Set if charging is required to begin the download
                    .setAllowedOverMetered(true) // Set if download is allowed on Mobile network
                    .setAllowedOverRoaming(true) // Set if download is allowed on roaming network
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOCUMENTS,
                        "bulkMail/test.jpg");

            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            Constants.downloadID =
                downloadManager.enqueue(request) // enqueue puts the download request in the queue
        }
    }
}