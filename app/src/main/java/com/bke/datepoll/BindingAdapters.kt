package com.bke.datepoll

import android.widget.EditText

fun bindInt(view: EditText, amount: Int) {
    view.setText("$amount")
}