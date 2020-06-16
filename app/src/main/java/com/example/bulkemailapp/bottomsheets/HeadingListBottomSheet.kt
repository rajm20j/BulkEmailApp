package com.example.bulkemailapp.bottomsheets

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.bulkemailapp.R
import kotlinx.android.synthetic.main.fragment_heading_list_bottom_sheet.*
import java.io.Serializable


class HeadingListBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_heading_list_bottom_sheet,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_head_list.layoutManager =
            LinearLayoutManager(context)
        rv_head_list.adapter = ItemAdapter(listItems?.size!!)
    }

    private inner class ViewHolder internal constructor(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.fragment_heading_list_bottom_sheet_list,
                parent,
                false
            )
        ) {
        internal val text: TextView = itemView.findViewById(R.id.text)
    }

    private inner class ItemAdapter internal constructor(private val mItemCount: Int) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = listItems?.get(position)
        }

        override fun getItemCount(): Int {
            return mItemCount
        }
    }

    companion object {
        var listItems: List<String>? = null
        fun newInstance(listItems: List<String>): HeadingListBottomSheet {
            this.listItems = listItems
            return HeadingListBottomSheet()
        }
    }
}