package com.example.bulkemailapp.addMoreEmail.model

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bulkemailapp.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

class AddEmailAdapter() : RecyclerView.Adapter<AddEmailAdapter.ViewHolder>() {

    var context: Context? = null
    lateinit var listItems: MutableList<AddEmailListModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.email_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listPositionItem = listItems[position]
        holder.name.text = listPositionItem.name
        holder.email.text = listPositionItem.email
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val constraintLayout = itemView.findViewById(R.id.the_big_view) as MaterialCardView
        val email = itemView.findViewById(R.id.tv_email_id) as TextView
        val name = itemView.findViewById(R.id.tv_name) as TextView
    }

    constructor(context: Context?, listItems: MutableList<AddEmailListModel>) : this() {
        this.context = context
        this.listItems = listItems
    }
}
