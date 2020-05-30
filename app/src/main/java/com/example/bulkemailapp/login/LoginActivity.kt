package com.example.bulkemailapp.login

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.R
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.sendemail.SendEmailFragment
import com.example.bulkemailapp.utils.MailHelper
import com.example.bulkemailapp.utils.Utils
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    @Inject
    lateinit var loginVMFactory: LoginVMFactory

    @Inject
    lateinit var mailHelper: MailHelper

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        (application as MyApp).myComponent.doInjection(this)

//        supportActionBar?.hide()
        loginViewModel =
            ViewModelProvider(this, loginVMFactory).get(LoginViewModel::class.java)


        if (mailHelper.getUser()) {
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                replace(R.id.login_constraint, SendEmailFragment())
                commit()
            }
        } else {
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                replace(R.id.login_constraint, LoginFragment())
                commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.logout_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            Utils.logout(this)
            return true
        }
        if (item.itemId == R.id.dark_switch) {
            if (item.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            item.isChecked = !item.isChecked
        }
        return super.onOptionsItemSelected(item)
    }
}