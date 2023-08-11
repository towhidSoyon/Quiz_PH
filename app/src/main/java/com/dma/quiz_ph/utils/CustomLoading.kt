package com.dma.quiz_ph.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.widget.AppCompatTextView
import com.dma.quiz_ph.R


class CustomLoading(var context: Context) {
    var dialog: Dialog? = null
    fun showLoading(title: String?) {
        dialog = Dialog(context)
        dialog!!.setContentView(R.layout.custom_loading)
        dialog!!.setCancelable(false)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val txtTitle = dialog!!.findViewById<AppCompatTextView>(R.id.txtTitle)
        txtTitle.text = title
        dialog!!.create()
        dialog!!.show()
    }

    fun dismissLoading() {
        dialog!!.dismiss()
    }
}
