package com.rigle.servicehub.ui.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rigle.servicehub.App
import com.rigle.servicehub.BR
import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.databinding.ViewProgressSheetBinding
import com.rigle.servicehub.ui.base.connectivity.ConnectivityProvider
import com.rigle.servicehub.ui.welcome.WelcomeActivity
import com.rigle.servicehub.utils.Constants
import com.rigle.servicehub.utils.event.NoInternetSheet
import com.rigle.servicehub.utils.extension.UnAuthUser
import com.rigle.servicehub.utils.extension.hideKeyboard
import com.rigle.servicehub.utils.rxutils.EventBus
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject


abstract class BaseActivity<Binding : ViewDataBinding> : AppCompatActivity(),
    ConnectivityProvider.ConnectivityStateListener {
    val TAG = this.javaClass.simpleName
    private var progressSheet: ProgressSheet? = null
    open val onRetry: (() -> Unit)? = null
    lateinit var binding: Binding
    val app: App
        get() = application as App

    @Inject
    lateinit var connectivityProvider: ConnectivityProvider

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager
    private var noInternetSheet: NoInternetSheet? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout: Int = getLayoutResource()
        binding = DataBindingUtil.setContentView(this, layout)
        binding.setVariable(BR.vm, getViewModel())
        connectivityProvider.addListener(this)
        onCreateView()
        registerEventBus(getViewModel())
    }

    protected abstract fun getLayoutResource(): Int
    protected abstract fun getViewModel(): BaseViewModel
    protected abstract fun onCreateView()

    private fun registerEventBus(viewModel: BaseViewModel) {
        viewModel.compositeDisposable.add(
            EventBus.subscribe<UnAuthUser>()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showUnauthSheet()
                }, {
                    Log.e(TAG, it.message.toString())
                })
        )
    }

    private fun showUnauthSheet() {
        sharedPrefManager.clearUser()
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    fun showToast(msg: String? = "Something went wrong !!") {
        Toast.makeText(this, msg ?: "Showed null value !!", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    fun showLoading(s: Int) {
        showLoading(getString(s))
    }

    fun showLoading(s: String?) {
        if (progressSheet != null && progressSheet?.isVisible == true) {
            progressSheet?.showMessage(s)
        } else {
            progressSheet = ProgressSheet(object : ProgressSheet.BaseCallback {
                override fun onClick(view: View?) {}
                override fun onBind(bind: ViewProgressSheetBinding) {
                    progressSheet?.showMessage(s);
                }
            })
            progressSheet?.show(supportFragmentManager, progressSheet?.tag)
        }
    }


    fun hideLoading() {
        progressSheet?.dismissAllowingStateLoss()
        progressSheet = null
    }


    override fun onDestroy() {
        hideLoading()
        connectivityProvider.removeListener(this)
        super.onDestroy()
    }


    override fun onStateChange(state: ConnectivityProvider.NetworkState) {
        if (noInternetSheet == null) {
            noInternetSheet = NoInternetSheet()
            noInternetSheet?.isCancelable = false
        }
        if (!state.isDisconnected()) {
            if (noInternetSheet?.isVisible == true)
                noInternetSheet?.dismiss()
        } else {
            if (noInternetSheet?.isVisible == false)
                noInternetSheet?.show(supportFragmentManager, noInternetSheet?.getTag())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Constants.locationHandler?.onActivityResult(requestCode, resultCode, data)
    }

}