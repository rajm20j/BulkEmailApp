package com.example.bulkemailapp.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.example.bulkemailapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TextFormattingBottomSheet: BottomSheetDialogFragment() {
    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(
            R.layout.custom_formatting_sheet
            , container,
            false
        )
    }

    companion object {
        fun newInstance(): TextFormattingBottomSheet {
            return TextFormattingBottomSheet()
        }
    }
}