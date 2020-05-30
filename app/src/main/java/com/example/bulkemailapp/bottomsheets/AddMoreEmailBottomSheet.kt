package com.example.bulkemailapp.bottomsheets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.example.bulkemailapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddMoreEmailBottomSheet: BottomSheetDialogFragment() {
    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(
            R.layout.custom_add_email_dialog
            , container,
            false
        )
    }

    companion object {
        fun newInstance(): AddMoreEmailBottomSheet {
            return AddMoreEmailBottomSheet()
        }
    }
}