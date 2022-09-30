package com.rigle.servicehub.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.ui.base.connectivity.ConnectivityProvider
import com.rigle.servicehub.ui.main.MainActivity
import com.rigle.servicehub.utils.extension.hideKeyboard
import com.google.android.material.progressindicator.LinearProgressIndicator
import javax.inject.Inject

abstract class BaseFragment<Binding : ViewDataBinding> : Fragment() {
    lateinit var binding: Binding
    val parentActivity: BaseActivity<*>?
        get() = activity as? BaseActivity<*>

    @Inject
    lateinit var connectivityProvider: ConnectivityProvider

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onCreatedView(view, savedInstanceState)
    }

    fun attachEmptyViewNavigation(view: View) {
        if (view.id == R.id.btn_go) {
            val intent = MainActivity.newIntent(requireContext())
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout: Int = getLayoutResource()
        binding = DataBindingUtil.inflate(layoutInflater, layout, container, false)
        binding.setVariable(BR.vm, getViewModel())
        return binding.root
    }

    protected abstract fun getLayoutResource(): Int
    protected abstract fun getViewModel(): BaseViewModel
    protected abstract fun onCreatedView(view: View, savedInstanceState: Bundle?)
    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    fun showLoading(s: String?) {
        parentActivity?.showLoading(s)
    }

    fun showLoading(s: Int) {
        parentActivity?.showLoading(getString(s))
    }

    fun hideLoading() {
        parentActivity?.hideLoading()
        getHorizontalBar()?.let {
            it.visibility = View.INVISIBLE
        }
    }

    fun showLoading() {
        getHorizontalBar()?.let {
            it.visibility = View.VISIBLE
        }
    }

    open fun getHorizontalBar(): LinearProgressIndicator? {
        return null
    }

    override fun onDetach() {
        hideLoading()
        super.onDetach()
    }



}