package com.example.bulkemailapp.addMoreEmail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bulkemailapp.MyApp
import com.example.bulkemailapp.R
import com.example.bulkemailapp.addMoreEmail.model.AddEmailAdapter
import com.example.bulkemailapp.addMoreEmail.model.AddEmailListModel
import com.example.bulkemailapp.extra.SharedPrefHelper
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_add_email.*
import com.example.bulkemailapp.utils.isEmailId
import javax.inject.Inject

class AddEmailActivity : AppCompatActivity() {

    @Inject
    lateinit var addEmailVMFactory: AddEmailVMFactory

    lateinit var addEmailViewModel: AddEmailViewModel

    lateinit var adapter: AddEmailAdapter

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_email)
        (application as MyApp).myComponent.doInjection(this)

        addEmailViewModel =
            ViewModelProvider(this, addEmailVMFactory).get(AddEmailViewModel::class.java)

        addEmailViewModel.listResponse.observe(this, Observer {
            this.consumeTestResponse(it)
        })

        adapter = AddEmailAdapter(this, sharedPrefHelper.getEmailList())
        rv_emails.adapter = adapter
        rv_emails.layoutManager = LinearLayoutManager(this)

        if(sharedPrefHelper.getEmailList().isEmpty()) {
            addEmailViewModel.addToRv(AddEmailListModel())
        }
        rv_emails.adapter?.notifyDataSetChanged()

        btn_add_more.setOnClickListener {
            val str = rv_emails.getChildAt(adapter.listItems.size-1).findViewById<TextInputEditText>(R.id.et_email_id).text.toString()
            if(!TextUtils.isEmpty(str))
            {
                if(str.isEmailId())
                    addEmailViewModel.addToRv(AddEmailListModel())
                else
                    Toast.makeText(this,"Enter a valid email address", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun consumeTestResponse(isAdded: Boolean) {
        if(isAdded)
            adapter.notifyDataSetChanged()
        else
            Toast.makeText(this, "Unable to add email address", Toast.LENGTH_LONG).show()
    }
}
