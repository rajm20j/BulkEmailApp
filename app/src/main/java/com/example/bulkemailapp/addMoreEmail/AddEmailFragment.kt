package com.example.bulkemailapp.addMoreEmail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.R
import com.example.bulkemailapp.addMoreEmail.model.AddEmailAdapter
import com.example.bulkemailapp.addMoreEmail.model.AddEmailListModel
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.example.bulkemailapp.login.LoginVMFactory
import com.example.bulkemailapp.login.LoginViewModel
import com.example.bulkemailapp.utils.AddEmailDialogListener
import com.example.bulkemailapp.utils.DialogHelper
import com.example.bulkemailapp.utils.Utils
import kotlinx.android.synthetic.main.fragment_add_email.*
import javax.inject.Inject

class AddEmailFragment : Fragment(R.layout.fragment_add_email), AddEmailDialogListener {
    @Inject
    lateinit var loginVMFactory: LoginVMFactory

    lateinit var loginViewModel: LoginViewModel

    lateinit var adapter: AddEmailAdapter

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity?.application as MyApp).myComponent.doInjection(this)

        loginViewModel =activity?.run {
            ViewModelProvider(this, loginVMFactory).get(LoginViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        loginViewModel.responseAddEmailRv.observe(viewLifecycleOwner, Observer {
            this.consumeTestResponse(it)
        })

        adapter = AddEmailAdapter(activity?.baseContext, sharedPrefHelper.getEmailList())
        rv_emails.adapter = adapter
        rv_emails.layoutManager = LinearLayoutManager(activity?.baseContext)

        if(sharedPrefHelper.getEmailList().isEmpty()) {
        }
        rv_emails.adapter?.notifyDataSetChanged()

        btn_add_more.setOnClickListener {
            val dialog = DialogHelper(activity!!.baseContext, this)
            dialog.getAddEmailSlideUp(activity!!)
        }

        btn_submit.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

            }
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun consumeTestResponse(isAdded: Boolean) {
        if(isAdded)
            adapter.notifyDataSetChanged()
        else
            Utils.generateSnackbarShort(coord_add_email, "Unable to add email")
    }

    override fun getEmailDialogBox(email: String, name: String) {
        val item = AddEmailListModel()
        item.email = email
        item.name = name
        loginViewModel.addToRv(item)
    }

    override fun dismissDialog() {
        rv_emails.adapter?.notifyDataSetChanged()
    }
}