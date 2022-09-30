package com.rigle.servicehub.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rigle.servicehub.BR
import com.rigle.servicehub.databinding.ViewProgressSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProgressSheet(val callback: BaseCallback) : BottomSheetDialogFragment() {
    private var binding: ViewProgressSheetBinding? = null
    private var message: String? = null

    companion object {
        const val TAG = "ProgressSheet"

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewProgressSheetBinding.inflate(inflater)
        binding?.setVariable(BR.callback, callback)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.let {
            callback.onBind(it)
        }
        showMessage()
    }

    fun showMessage(s: String?) {
        message = s
        showMessage()
    }

    private fun showMessage() {
        binding?.let {
            it.tvMessage.text = message
        }
    }

    interface BaseCallback {
        fun onClick(view: View?)
        fun onBind(bind: ViewProgressSheetBinding)
    }

}