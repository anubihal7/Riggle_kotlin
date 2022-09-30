package com.rigle.servicehub.ui.base.sheet

import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.rigle.servicehub.BR


class BaseSheet<B : ViewDataBinding> : BottomSheetDialogFragment {
    private lateinit var binding: B
    private var listener: ClickListener<B>? = null
    private val layout: Int

    constructor(@LayoutRes layout: Int) {
        this.layout = layout
    }

    constructor(layout: Int, listener: ClickListener<B>) {
        this.listener = listener
        this.layout = layout
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.setVariable(BR.callback, listener)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.onViewCreated(binding)
    }

    interface ClickListener<B> {
        fun onClick(view: View)
        fun onViewCreated(b: B)
    }
}