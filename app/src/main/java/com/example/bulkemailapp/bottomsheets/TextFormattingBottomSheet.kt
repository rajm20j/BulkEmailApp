package com.example.bulkemailapp.bottomsheets

import android.content.DialogInterface
import android.graphics.Typeface.*
import android.os.Bundle
import android.text.Layout
import android.text.Spannable
import android.text.style.AlignmentSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.text.getSpans
import com.example.bulkemailapp.R
import com.example.bulkemailapp.data.SpannableList
import com.example.bulkemailapp.utils.Animations
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.custom_formatting_sheet.*
import javax.inject.Inject

class TextFormattingBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var spannableList: SpannableList

    private var textSizeRatio = 1f

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeClickListeners()

    }

    private fun initializeClickListeners() {
        font_style_toggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (view?.findViewById<MaterialButton>(checkedId) == btn_bold) {
                boldSelection(btn_bold.isChecked)
            }
            if (view?.findViewById<MaterialButton>(checkedId) == btn_italic) {
                italicSelection(btn_italic.isChecked)
            }
            if (view?.findViewById<MaterialButton>(checkedId) == btn_underline) {
                underlineSelection(btn_underline.isChecked)
            }
        }

        alignment_toggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (view?.findViewById<MaterialButton>(checkedId) == btn_align_left) {
                btn_align_left.isChecked = false
                alignLeft()
            }
            if (view?.findViewById<MaterialButton>(checkedId) == btn_align_center) {
                btn_align_center.isChecked = false
                alignCenter()
            }
            if (view?.findViewById<MaterialButton>(checkedId) == btn_align_right) {
                btn_align_right.isChecked = false
                alignRight()
            }
        }

        btn_text_size.setOnClickListener {
            if(btn_text_size.isChecked)
            {
                Animations.setScrollUpAnimation(activity?.baseContext, text_size_toggle, 150)
                text_size_toggle.visibility = View.VISIBLE
            }
            else
            {
                text_size_toggle.visibility = View.GONE
            }
        }

        text_size_toggle.addOnButtonCheckedListener { _, checkedId, _ ->
            if (view?.findViewById<MaterialButton>(checkedId) == btn_size_minus_2) {
                btn_size_minus_2.isChecked = false
                increaseFontSize(0.8f)
                textSizeRatio *= 0.8f
            }
            if (view?.findViewById<MaterialButton>(checkedId) == btn_size_minus_1) {
                btn_size_minus_1.isChecked = false
                increaseFontSize(0.9f)
                textSizeRatio *= 0.9f
            }
            if (view?.findViewById<MaterialButton>(checkedId) == btn_size_0) {
                btn_size_0.isChecked = false
                increaseFontSize(1/textSizeRatio)
                textSizeRatio = 1f
            }
            if (view?.findViewById<MaterialButton>(checkedId) == btn_size_plus_1) {
                btn_size_plus_1.isChecked = false
                increaseFontSize(1.1f)
                textSizeRatio *= 1.1f
            }
            if (view?.findViewById<MaterialButton>(checkedId) == btn_size_plus_2) {
                btn_size_plus_2.isChecked = false
                increaseFontSize(1.2f)
                textSizeRatio *= 1.2f
            }
        }
    }

    private fun boldSelection(isBold: Boolean) {
        val selectionStart = editText!!.selectionStart
        val selectionEnd = editText!!.selectionEnd
        if (isBold) {
            editText?.text?.setSpan(
                StyleSpan(BOLD),
                selectionStart, selectionEnd,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        } else {
            val spanned = editText?.text?.getSpans<StyleSpan>(selectionStart, selectionEnd)
            for (selectedSpan in spanned!!) {
                if (selectedSpan.style == BOLD)
                    editText?.text?.removeSpan(selectedSpan)
            }
        }
    }

    private fun italicSelection(isItalic: Boolean) {
        val selectionStart = editText!!.selectionStart
        val selectionEnd = editText!!.selectionEnd
        if (isItalic) {
            editText?.text?.setSpan(
                StyleSpan(ITALIC),
                selectionStart, selectionEnd,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        } else {
            val spanned = editText?.text?.getSpans<StyleSpan>(selectionStart, selectionEnd)
            for (selectedSpan in spanned!!) {
                if (selectedSpan.style == ITALIC)
                    editText?.text?.removeSpan(selectedSpan)
            }
        }
    }

    private fun underlineSelection(isUnderlined: Boolean) {
        val selectionStart = editText!!.selectionStart
        val selectionEnd = editText!!.selectionEnd
        if (isUnderlined) {
            editText?.text?.setSpan(
                UnderlineSpan(),
                selectionStart, selectionEnd,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        } else {
            val spanned = editText?.text?.getSpans<UnderlineSpan>(selectionStart, selectionEnd)
            for (selectedSpan in spanned!!) {
                    editText?.text?.removeSpan(selectedSpan)
            }
        }
    }

    private fun alignRight()
    {
        val selectionStart = editText!!.selectionStart
        val selectionEnd = editText!!.selectionEnd
        editText?.text?.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),
            selectionStart, selectionEnd,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }

    private fun alignLeft()
    {
        val selectionStart = editText!!.selectionStart
        val selectionEnd = editText!!.selectionEnd
        editText?.text?.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL),
            selectionStart, selectionEnd,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }

    private fun alignCenter()
    {
        val selectionStart = editText!!.selectionStart
        val selectionEnd = editText!!.selectionEnd
        editText?.text?.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
            selectionStart, selectionEnd,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }

    private fun increaseFontSize(size: Float)
    {
        val selectionStart = editText!!.selectionStart
        val selectionEnd = editText!!.selectionEnd
        editText?.text?.setSpan(
            RelativeSizeSpan(size),
            selectionStart, selectionEnd,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    companion object {
        var editText: TextInputEditText? = null
        fun newInstance(editText: TextInputEditText?): TextFormattingBottomSheet {
            this.editText = editText
            return TextFormattingBottomSheet()
        }
    }
}