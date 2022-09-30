package com.rigle.servicehub.utils

import android.content.Context
import com.rigle.servicehub.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object UtilMethods {
    fun getCurrencyFormat(context: Context?, amount: Double?, showDecimal: Boolean = true): String {
        var amt = amount.toString()
        try {
            if (showDecimal) {
                amt =
                    context?.getString(R.string.rs_symbol) + " " + DecimalFormat("#,##,##,##0.00").format(
                        amount
                    )
            } else {
                amt =
                    context?.getString(R.string.rs_symbol) + " " + DecimalFormat("#,##,##,##0").format(
                        amount
                    )
            }
        } catch (e: Exception) {
        }
        return amt
    }


    fun getNumberFormat(context: Context?, amount: Float?): String {
        return try {
            if (amount != null) DecimalFormat("#,##,##,##00").format(amount) else "00"
        } catch (e: Exception) {
            "" + amount
        }
    }

    fun getCalenderObject( pattren: String, time: String?): Date? {
        try {
            time?.let {
                val s = SimpleDateFormat(pattren, Locale.US).parse(time)
                return s
            }
        } catch (e: Exception) {

        }
        return null
    }
}