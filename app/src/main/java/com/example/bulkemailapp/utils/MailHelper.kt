package com.example.bulkemailapp.utils

import android.util.Log
import com.example.bulkemailapp.extra.Constants
import com.example.bulkemailapp.extra.SharedPrefHelper
import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class MailHelper {
    var session: Session? = null
    var user = mutableMapOf<String, String?>()


    fun setUser(username: String, password: String) {
        user[Constants.senderEmail] = username
        SharedPrefHelper.setStr(Constants.senderEmail, username)

        user[Constants.senderPass] = password
        SharedPrefHelper.setStr(Constants.senderPass, password)
    }

    fun getUser(): Boolean {
        if (SharedPrefHelper.getStr(Constants.senderEmail) != "NULL" && SharedPrefHelper.getStr(
                Constants.senderPass
            ) != "NULL"
        ) {
            user[Constants.senderEmail] = SharedPrefHelper.getStr(Constants.senderEmail)
            user[Constants.senderPass] = SharedPrefHelper.getStr(Constants.senderPass)
            return true
        }
        return false
    }

    fun testConnection(host: String, port: String): String {
        if(session == null)
            generateSession(host, port)
        return try {
            val transport = session?.getTransport("smtp")
            transport?.connect(host, user[Constants.senderEmail], user[Constants.senderPass])
            transport?.close()
            session = null
            Constants.True
        } catch (e: Exception) {
            Log.v("MAINNN", e.toString())
            e.printStackTrace()
            Constants.False
        }
    }

    fun generateSession(host: String, port: String) {
        val properties = Properties()
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.host"] = host
        properties["mail.smtp.port"] = port

        session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(
                    user[Constants.senderEmail],
                    user[Constants.senderPass]
                )
            }
        })
    }

    fun generateMessage(recipient: String, subject: String, body: String): Message {
        val message: Message = MimeMessage(session)
        try {
            message.setFrom(InternetAddress(Constants.senderEmail))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient.trim()))
            message.subject = subject.trim()
            message.setText(body.trim())
            message
        } catch (e: Exception) {
            e.printStackTrace()
            Log.v("MAINN", e.toString())
        }
        return message
    }

    fun removeUser()
    {
        SharedPrefHelper.clrStr()
        user.clear()
    }
}