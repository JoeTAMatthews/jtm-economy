package com.jtmnetwork.economy.core.util

import java.text.SimpleDateFormat
import java.util.*

class UtilTime {
    companion object {
        fun formatTime(time: Long): String {
            val format = SimpleDateFormat("dd-MM-yyyy HH:mm")
            return format.format(Date(time))
        }
    }
}