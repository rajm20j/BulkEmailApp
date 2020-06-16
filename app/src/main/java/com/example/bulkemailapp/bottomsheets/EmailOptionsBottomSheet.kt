package com.example.bulkemailapp.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.example.bulkemailapp.R
import com.example.bulkemailapp.addMoreEmail.AddEmailFragment
import com.example.bulkemailapp.utils.DialogHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.custom_email_option.*

class EmailOptionsBottomSheet: BottomSheetDialogFragment() {
    lateinit var listItem: List<String>

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(
            R.layout.custom_email_option
            , container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeClickListeners()
    }

    private fun initializeClickListeners() {
        add_to_list.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                replace(R.id.login_constraint, AddEmailFragment())
                addToBackStack(null)
                commit()
            }
            dismiss()
        }

        via_csv.setOnClickListener {
            val dialogHelper = DialogHelper()
            dialogHelper.getHeadingListSlideUp(activity!!, listItem)
            dismiss()
        }
    }

    companion object {
        fun newInstance(listItem: List<String>): EmailOptionsBottomSheet {
            val emailOptionsBottomSheet = EmailOptionsBottomSheet()
            emailOptionsBottomSheet.listItem = listItem
            return emailOptionsBottomSheet
        }
    }
}