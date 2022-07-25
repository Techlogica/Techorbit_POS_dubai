package com.example.techorbit.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.techorbit.R
import kotlinx.android.synthetic.main.layout_transaction_single.view.*

@SuppressLint("ResourceAsColor")
@BindingAdapter("binddata")
fun binddata(view: TextView, value: String) {
    if (value == "FAILED" || value == "CANCELLED" || value == "FAILED_BY_ERROR" || value == "CANCELLING") {
        view.text = value
        view.setTextColor(Color.WHITE)
    } else if (value == "SUCCESS"){
        view.text = value
        view.setTextColor(Color.WHITE)
    }else {
        view.text = value
        view.setTextColor(Color.WHITE)
    }
}

@BindingAdapter("paidto")
fun bindPaid(view: TextView, value: String) {
    val text = value.removeRange(0, 5)
    view.text = text
}