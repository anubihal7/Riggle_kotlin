package com.rigle.servicehub.ui.welcome

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.RigleConstantsResponse
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.ActivityWelcomeBinding
import com.rigle.servicehub.ui.base.BaseActivity
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.info.OnBoardInfoActivity
import com.rigle.servicehub.ui.introscreen.IntroActivity
import com.rigle.servicehub.ui.main.MainActivity
import com.rigle.servicehub.utils.Constants
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.showErrorToast
import com.rigle.servicehub.utils.extension.startNewActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {

    private val viewModel: WelcomeActivityVM by viewModels()

    override fun getLayoutResource(): Int {
        return R.layout.activity_welcome
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
    }
    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onStart() {
        super.onStart()
        viewModel.getConstants()
    }

    override fun onCreateView() {
        viewModel.obrConstants.observe(
            this,
            SingleRequestEvent.RequestObserver<RigleConstantsResponse> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        binding.pbOne.animate().alpha(0.5f)
                    }
                    Status.SUCCESS -> {
                        binding.pbOne.animate().alpha(0f)
                        openActivity()
                    }
                    Status.ERROR -> {
                        binding.pbOne.animate().alpha(0f)
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {

                    }
                }
            })

        //checkLocation()
    }

    private fun openActivity() {
        val user = sharedPrefManager.getCurrentUser()
        if (user != null) {
            Log.e(TAG, "Session=====>" + sharedPrefManager.getSession())
            Log.e(TAG, "User=====>" + Gson().toJson(user))
            if (user.company?.accountStatus == Constants.ACCOUNT_STATUS_PENDING) {
                startNewActivity(OnBoardInfoActivity::class.java, true)
            } else
                startNewActivity(MainActivity::class.java, true)
        } else {
            startNewActivity(IntroActivity::class.java, true)
        }
    }


}