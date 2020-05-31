package com.example.bulkemailapp.data

import android.text.SpannableString
import com.example.bulkemailapp.extra.Constants
import java.util.*

class SpannableList {
    var list: Deque<SpannableString> = LinkedList()

    fun addLast(e: SpannableString) {
        if (list.size > Constants.maxSpanStackSize)
            list.pollFirst()
        list.offerLast(e)
    }

    fun getLast(): SpannableString? {
        return list.pollLast()
    }
}