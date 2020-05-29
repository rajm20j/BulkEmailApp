package com.example.bulkemailapp.addMoreEmail.model

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bulkemailapp.R
import com.google.android.material.textfield.TextInputEditText

class AddEmailAdapter() : RecyclerView.Adapter<AddEmailAdapter.ViewHolder>() {

    lateinit var context: Context
    lateinit var listItems: MutableList<AddEmailListModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.email_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listPositionItem = listItems[position]
        holder.name.setText(listPositionItem.name)
        holder.email.setText(listPositionItem.email)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val constraintLayout = itemView.findViewById(R.id.the_big_view) as ConstraintLayout
        val email = itemView.findViewById(R.id.et_email_id) as TextInputEditText
        val name = itemView.findViewById(R.id.et_name) as TextInputEditText
    }

    constructor(context: Context, listItems: MutableList<AddEmailListModel>) : this() {
        this.context = context
        this.listItems = listItems
    }
}
