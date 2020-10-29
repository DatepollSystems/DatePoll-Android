package com.datepollsystems.datepoll.utils

import com.datepollsystems.datepoll.components.main.event.model.EventDbModel

class ClickListener(val clickListener: () -> Unit) {
    fun onClick() = clickListener()
}