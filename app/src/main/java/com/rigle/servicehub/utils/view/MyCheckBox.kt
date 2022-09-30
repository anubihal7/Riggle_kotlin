package com.rigle.servicehub.utils.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.rigle.servicehub.R

class MyCheckBox : AppCompatImageView {
     var checked = false
        set(value) {
            field = value
            updateView()
        }

    var onCheckChangeListener: OnCheckChangeListener? = null

    constructor(context: Context) : super(context) {
        updateView()
        addClickListioner()
    }


    private fun addClickListioner() {
        setOnClickListener {
            checked = !checked
            updateView()
            onCheckChangeListener?.onCheckChanged(checked)
        }
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        addClickListioner()
        updateView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addClickListioner()
        updateView()
    }

    private fun updateView() {
        adjustViewBounds = true
        scaleType = ScaleType.FIT_START
        if (checked) {
            setImageResource(R.drawable.ic_checked)
        } else {
            setImageResource(R.drawable.ic_un_check)
        }
    }

    interface OnCheckChangeListener {
        fun onCheckChanged(checked: Boolean)
    }
}