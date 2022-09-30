package com.rigle.servicehub.binding

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.rigle.servicehub.R
import com.rigle.servicehub.utils.Constants
import com.rigle.servicehub.utils.UtilMethods
import java.text.SimpleDateFormat
import java.util.*

object TextViewBindings {

    @JvmStatic
    @BindingAdapter(value = ["currency"], requireAll = false)
    fun currency(
        textView: TextView,
        amount: Double,

        ) {
        textView.text = UtilMethods.getCurrencyFormat(textView.context, amount)
    }

    @JvmStatic
    @BindingAdapter(value = ["status_boy"], requireAll = false)
    fun statusDeliveryBoy(
        textView: TextView,
        status: Boolean,
    ) {
        if (status) {
            textView.text = "Active"
            textView.setTextColor(Color.GREEN)
        } else {
            textView.text = "InActive"
            textView.setTextColor(Color.RED)
        }

    }

    @JvmStatic
    @BindingAdapter(value = ["date_time"], requireAll = false)
    fun dateTime(
        textView: TextView,
        time: String,

        ) {
        //2022-01-10T19:28:16
        try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val s = simpleDateFormat.parse(time)
            if (s != null)
                textView.text = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.US).format(s)
        } catch (e: Exception) {
            textView.text = time
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["week_date"], requireAll = false)
    fun weekdate(
        textView: TextView,
        time: String,
    ) {
        //2022-01-10T19:28:16
        try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val s = simpleDateFormat.parse(time)
            if (s != null)
                textView.text = SimpleDateFormat("EEEE dd MMM yyyy", Locale.US).format(s)
        } catch (e: Exception) {
            textView.text = time
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["order_status"], requireAll = false)
    fun orderStatus(
        textView: TextView,
        status: String?,
    ) {
        if (status != null) {
            var i = R.color.black
            var y =R.string.error
            when (status) {
                Constants.STATUS_PENDING -> {
                    i = R.color.back_pending
                    y = R.string.pending
                }
                Constants.STATUS_DELIVERED,
                -> {
                    i = R.color.back_complete
                    y = R.string.delivered
                }
                Constants.STATUS_COMPLETED
                -> {
                    i = R.color.back_complete
                    y = R.string.completed
                }
                Constants.STATUS_CANCELLED -> {
                    i = R.color.back_cancel
                    y = R.string.cancelled
                }
                Constants.STATUS_CONFIRMED -> {
                    i = R.color.back_confirm
                    y = R.string.confirm
                }
                Constants.STATUS_OUTSTANDING -> {
                    i = R.color.back_outstand
                    y = R.string.outs_amount
                }
            }
            textView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    textView.context,
                    i
                )
            )
            textView.setText(y)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["order_status_line"], requireAll = false)
    fun orderStatusLine(
        textView: View,
        status: String?,
    ) {
        if (status != null) {
            val color = when (status) {
                Constants.STATUS_PENDING -> R.color.status_pending
                Constants.STATUS_DELIVERED,
                Constants.STATUS_COMPLETED -> R.color.status_completed
                Constants.STATUS_CANCELLED -> R.color.status_cancel
                Constants.STATUS_CONFIRMED -> R.color.status_pending
                Constants.STATUS_OUTSTANDING -> R.color.status_outstanding
                else -> R.color.black
            }
            textView.setBackgroundColor(
                ContextCompat.getColor(
                    textView.context, color
                )
            )
        }
    }
}


