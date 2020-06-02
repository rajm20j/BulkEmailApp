package com.example.bulkemailapp.bottomsheets

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface.*
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.text.Spannable
import android.text.style.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.text.getSpans
import androidx.core.view.get
import androidx.core.view.size
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
    private val maxTextRatio = 5f
    private val minTextRatio = 1 / 5f

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

        text_size_toggle.addOnButtonCheckedListener { _, checkedId, _ ->
            if (view?.findViewById<MaterialButton>(checkedId) == btn_size_minus_2) {
                btn_size_minus_2.isChecked = false
                if (textSizeRatio * 0.8f >= minTextRatio) {
                    increaseFontSize(0.8f)
                    textSizeRatio *= 0.8f
                }
            }
            if (view?.findViewById<MaterialButton>(checkedId) == btn_size_minus_1) {
                btn_size_minus_1.isChecked = false
                if (textSizeRatio * 0.9f >= minTextRatio) {
                    increaseFontSize(0.9f)
                    textSizeRatio *= 0.9f
                }
            }
            if (view?.findViewById<MaterialButton>(checkedId) == btn_size_0) {
                btn_size_0.isChecked = false
                increaseFontSize(1 / textSizeRatio)
                textSizeRatio = 1f
            }
            if (view?.findViewById<MaterialButton>(checkedId) == btn_size_plus_1) {
                btn_size_plus_1.isChecked = false
                if (textSizeRatio * 1.1f <= maxTextRatio) {
                    increaseFontSize(1.1f)
                    textSizeRatio *= 1.1f
                }
            }
            if (view?.findViewById<MaterialButton>(checkedId) == btn_size_plus_2) {
                btn_size_plus_2.isChecked = false
                if (textSizeRatio * 1.2f <= maxTextRatio) {
                    increaseFontSize(1.2f)
                    textSizeRatio *= 1.2f
                }
            }
        }

        font_toggle_group.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (group.checkedButtonId) {
                -1 -> {
                    text_size_toggle.visibility = View.GONE
                    text_color_toggle.visibility = View.GONE
                }
                R.id.btn_text_size -> {
                    text_color_toggle.visibility = View.GONE

                    Animations.setScrollUpAnimation(activity?.baseContext, text_size_toggle, 150)
                    text_size_toggle.visibility = View.VISIBLE
                }
                R.id.btn_text_color -> {
                    text_size_toggle.visibility = View.INVISIBLE

                    btn_transparent.visibility = View.GONE

                    Animations.setScrollUpAnimation(activity?.baseContext, text_color_toggle, 150)
                    text_color_toggle.visibility = View.VISIBLE
                }
                R.id.btn_text_highlight -> {
                    text_size_toggle.visibility = View.INVISIBLE

                    btn_transparent.visibility = View.VISIBLE

                    Animations.setScrollUpAnimation(activity?.baseContext, text_color_toggle, 150)
                    text_color_toggle.visibility = View.VISIBLE
                }
            }
        }

        text_color_toggle.addOnButtonCheckedListener { _, checkedId, _ ->
            if (view?.findViewById<MaterialButton>(checkedId) == btn_transparent) {
                if(btn_text_color.isChecked)
                    colorText(Color.TRANSPARENT)
                else if (btn_text_highlight.isChecked)
                    highlightText(Color.TRANSPARENT)
            }

            if (view?.findViewById<MaterialButton>(checkedId) == btn_black) {
                if(btn_text_color.isChecked)
                    colorText(Color.BLACK)
                else if (btn_text_highlight.isChecked)
                    highlightText(Color.BLACK)
            }

            if (view?.findViewById<MaterialButton>(checkedId) == btn_white) {
                if(btn_text_color.isChecked)
                    colorText(Color.WHITE)
                else if (btn_text_highlight.isChecked)
                    highlightText(Color.WHITE)
            }

            if (view?.findViewById<MaterialButton>(checkedId) == btn_v) {
                val color: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resources.getColor(R.color.violet, null)
                } else
                    resources.getColor(R.color.violet)

                if(btn_text_color.isChecked){
                    colorText(color)
                }
                else if (btn_text_highlight.isChecked)
                    highlightText(color)
            }

            if (view?.findViewById<MaterialButton>(checkedId) == btn_i) {
                val color: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resources.getColor(R.color.indigo, null)
                } else
                    resources.getColor(R.color.indigo)

                if(btn_text_color.isChecked){
                    colorText(color)
                }
                else if (btn_text_highlight.isChecked)
                    highlightText(color)
            }

            if (view?.findViewById<MaterialButton>(checkedId) == btn_b) {
                val color: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resources.getColor(R.color.blue, null)
                } else
                    resources.getColor(R.color.blue)

                if(btn_text_color.isChecked){
                    colorText(color)
                }
                else if (btn_text_highlight.isChecked)
                    highlightText(color)
            }

            if (view?.findViewById<MaterialButton>(checkedId) == btn_g) {
                val color: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resources.getColor(R.color.green, null)
                } else
                    resources.getColor(R.color.green)

                if(btn_text_color.isChecked){
                    colorText(color)
                }
                else if (btn_text_highlight.isChecked)
                    highlightText(color)
            }

            if (view?.findViewById<MaterialButton>(checkedId) == btn_y) {
                val color: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resources.getColor(R.color.yellow, null)
                } else
                    resources.getColor(R.color.yellow)

                if(btn_text_color.isChecked){
                    colorText(color)
                }
                else if (btn_text_highlight.isChecked)
                    highlightText(color)
            }

            if (view?.findViewById<MaterialButton>(checkedId) == btn_o) {
                val color: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resources.getColor(R.color.orange, null)
                } else
                    resources.getColor(R.color.orange)

                if(btn_text_color.isChecked){
                    colorText(color)
                }
                else if (btn_text_highlight.isChecked)
                    highlightText(color)
            }

            if (view?.findViewById<MaterialButton>(checkedId) == btn_r) {
                val color: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resources.getColor(R.color.red, null)
                } else
                    resources.getColor(R.color.red)

                if(btn_text_color.isChecked){
                    colorText(color)
                }
                else if (btn_text_highlight.isChecked)
                    highlightText(color)
            }
        }
    }

    private fun colorText(color: Int) {
        val selectionStart = editText!!.selectionStart
        val selectionEnd = editText!!.selectionEnd

        editText?.text?.setSpan(
            ForegroundColorSpan(color),
            selectionStart, selectionEnd,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }

    private fun highlightText(color: Int) {
        val selectionStart = editText!!.selectionStart
        val selectionEnd = editText!!.selectionEnd

        editText?.text?.setSpan(
            BackgroundColorSpan(color),
            selectionStart, selectionEnd,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
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

    private fun alignRight() {
        val selectionStart = editText!!.selectionStart
        val selectionEnd = editText!!.selectionEnd
        editText?.text?.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),
            selectionStart, selectionEnd,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }

    private fun alignLeft() {
        val selectionStart = editText!!.selectionStart
        val selectionEnd = editText!!.selectionEnd
        editText?.text?.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL),
            selectionStart, selectionEnd,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }

    private fun alignCenter() {
        val selectionStart = editText!!.selectionStart
        val selectionEnd = editText!!.selectionEnd
        editText?.text?.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
            selectionStart, selectionEnd,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }

    private fun increaseFontSize(size: Float) {
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