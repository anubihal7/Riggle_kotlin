package com.rigle.servicehub.ui.base.sheet

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.databinding.SheetBlurBaseBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


class BaseCloseSheet<B : ViewDataBinding>(
    val layout: Int,
    val listener: ClickListener<B>,
    val showBar: Boolean = true
) :
    BottomSheetDialogFragment() {
    private lateinit var binding: B

    fun getBinding(): B {
        return binding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                dialog.behavior.peekHeight = sheet.height
                sheet.parent.parent.requestLayout()
            }
        }
        val bmain = DataBindingUtil.inflate<SheetBlurBaseBinding>(
            inflater,
            R.layout.sheet_blur_base, container, false
        )
        bmain.vBar.visibility = if (showBar) View.VISIBLE else View.GONE
        bmain.ivCross.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding = DataBindingUtil.inflate(inflater, layout, bmain.main, false)
        binding.setVariable(BR.callback, listener)
        bmain.main.addView(binding.root)
        return bmain.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.onClose()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener.onViewCreated(binding)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.MyTransparentBottomSheetDialogTheme)
    }


    interface ClickListener<B> {
        fun onClick(view: View)
        fun onViewCreated(b: B)
        fun onClose()
    }
}