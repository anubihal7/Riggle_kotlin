package com.rigle.servicehub.ui.base.sheet

import android.view.View
import com.rigle.servicehub.R
import com.rigle.servicehub.databinding.SheetMessageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MessageSheetBuilder(
    private var title: String? = null,
    private var message: String? = null,
    private var positive: String? = null,
    private var negative: String? = null,
    private var listener: Listener? = null,
) {
    fun setTitleText(title: String?): MessageSheetBuilder {
        this.title = title
        return this
    }

    fun setMessageText(message: String?): MessageSheetBuilder {
        this.message = message
        return this
    }

    fun setPositiveButton(positive: String?): MessageSheetBuilder {
        this.positive = positive
        return this
    }

    fun setNegativeButton(negative: String?): MessageSheetBuilder {
        this.negative = negative
        return this
    }

    fun setListener(listener: Listener): MessageSheetBuilder {
        this.listener = listener
        return this
    }

    private lateinit var sheetView: BaseSheet<SheetMessageBinding>


    fun build(): BaseSheet<SheetMessageBinding> {
        sheetView = BaseSheet(
            R.layout.sheet_message,
            object : BaseSheet.ClickListener<SheetMessageBinding> {
                override fun onClick(view: View) {
                    when (view.id) {
                        R.id.tv_ok -> {
                            sheetView.let { listener?.onPositiveButtonClick(it) }
                        }
                        R.id.tv_cancel -> {
                            sheetView.let { listener?.onNegativeButtonClick(it) }
                        }
                    }
                }

                override fun onViewCreated(b: SheetMessageBinding) {
                    b.title = title
                    b.message = message
                    b.textOk = positive
                    b.textCancel = negative
                }

            })

        return sheetView

    }

    interface Listener {
        fun onPositiveButtonClick(sheet: BottomSheetDialogFragment)
        fun onNegativeButtonClick(sheet: BottomSheetDialogFragment) {
            sheet.dismissAllowingStateLoss()
        }
    }

}