package com.har.pianoclassic.utils

import android.content.Context
import com.har.pianoclassic.R
import android.view.*
import android.widget.*

open class CustomToastUtils(view: View, context: Context?) {

    private var toast: Toast
    private var textView: TextView

    init {
        textView = view.findViewById(R.id.text)
        toast = Toast(context)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.view = view
    }

    @JvmName("setText1")
    fun setText(text: String?) {
        textView.text = text
    }

    fun show() {
        toast.show()
    }

}