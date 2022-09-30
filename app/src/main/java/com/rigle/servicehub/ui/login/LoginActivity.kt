package com.rigle.servicehub.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.activity.viewModels
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.ActivityLoginBinding
import com.rigle.servicehub.databinding.SheetAddCompanyBinding
import com.rigle.servicehub.ui.base.BaseActivity
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.sheet.BaseCloseSheet
import com.rigle.servicehub.ui.welcome.WelcomeActivity
import com.rigle.servicehub.utils.callbacks.Validator
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private var alreadyExists = false
    private var sheet1: BaseCloseSheet<SheetAddCompanyBinding>? = null
    private var countDownTimer: CountDownTimer? = null
    val viewModel: LoginActivityVM by viewModels()
    private var name: String? = null
    private var pincode: String? = null
    private var fromregister = false

    companion object {
        fun newIntent(activity: Activity): Intent {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_login
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        fromregister = intent.getBooleanExtra("fromregister", false)
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.btn_next -> {
                    if (binding.vBottom.deactive)
                        return@observe
                    if (!binding.cbox.isChecked) {
                        showInfoToast("Accept terms and conditions!")
                        return@observe
                    }
                    if (isOtpView()) {
                        verifyOtp()

                    } else {
                        sendOtp()
                    }
                }
                R.id.tv_resend -> {
                    sendOtp()
                }
            }
        }
        viewModel.obrverify.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> showLoading(R.string.plz_wait)
                    Status.SUCCESS -> {
                        hideLoading()
                        resource.data?.let {
                            startNewActivity(WelcomeActivity::class.java, true)
                        }
                    }
                    else -> {
                        hideLoading()
                        showErrorToast(resource.message)
                    }
                }
            })

        viewModel.obrOtp.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> showLoading(R.string.plz_wait)
                    Status.SUCCESS -> {
                        hideLoading()
                        resource.data?.asJsonObject?.get("data")?.asJsonObject?.get("already_exists")?.asBoolean?.let {
                            alreadyExists = it
                        }
                        if (alreadyExists) {
                            if (fromregister) {
                                showInfoToast("EmailId already exists")
                            } else {
                                resource.data?.asJsonObject?.get("message")?.asString?.let {
                                    showSuccessToast(it)
                                }
                                showOtpView()
                                startTimer()
                            }
                        } else {
                            showSheet()
                        }
                    }
                    else -> {
                        hideLoading()
                        showErrorToast(resource.message)
                    }
                }
            })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.terms.text =
                Html.fromHtml(
                    getString(R.string.terms_message),
                    Html.FROM_HTML_MODE_COMPACT
                )
        };
        binding.terms.movementMethod = LinkMovementMethod.getInstance();
        binding.vBottom.tvSkip.visibility = View.INVISIBLE
        binding.vBottom.deactive = true
        initPhone()
        hideOtpView()
    }

    private fun sendOtp() {
        hideOtpView()
        when {
            viewModel.field_phone.get().isNullOrBlank() -> {
                binding.etEmail.error = "Invalid"
            }
            else -> {
                val parm = JSONObject()
                parm.put("mobile", viewModel.field_phone.get().toString())
                viewModel.getOtp(parm)
            }
        }
    }

    private fun verifyOtp() {
        when {
            viewModel.field_otp.get().isNullOrBlank() -> {
                binding.etotp.error = "Invalid"
            }
            alreadyExists -> {
                val parm = JSONObject()
                parm.put("mobile", viewModel.field_phone.get().toString())
                parm.put("otp", viewModel.field_otp.get().toString())
                viewModel.verifyOtp(parm)
            }
            pincode.isNullOrBlank() -> {
                showSheet()
            }
            name.isNullOrBlank() -> {
                showSheet()
            }
            else -> {
                val parm = JSONObject()
                parm.put("mobile", viewModel.field_phone.get().toString())
                parm.put("otp", viewModel.field_otp.get().toString())
                parm.put("pincode", pincode)
                parm.put("name", name)
                viewModel.verifyOtp(parm)
            }
        }
    }

    private fun hideOtpView() {
        binding.tvotp.visibility = View.GONE
        binding.etotp.visibility = View.GONE
        binding.etEmail.isEnabled = true
    }

    private fun showOtpView() {
        binding.tvotp.visibility = View.VISIBLE
        binding.etotp.visibility = View.VISIBLE
        binding.etEmail.isEnabled = false
    }

    private fun isOtpView(): Boolean {
        return binding.tvotp.visibility == View.VISIBLE

    }

    private fun initPhone() {
        binding.etEmail.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
                return if (s.length == 10) {
                    if (!isOtpView()) {
                        binding.vBottom.deactive = false
                    }
                    true
                } else {
                    if (!isOtpView()) {
                        binding.vBottom.deactive = true
                    }
                    false
                }
            }
        })
        binding.etotp.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
                return (s.length == 6)
            }
        })

    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                showCountDown("Resend Otp in 0:${millisUntilFinished / 1000}")
            }

            override fun onFinish() {
                showCountDown(null)
            }


        }
        countDownTimer?.start()
    }

    private fun showCountDown(text: String?) {
        if (text != null) {
            binding.tvResend.visibility = View.GONE
            binding.countdown.visibility = View.VISIBLE
            binding.countdown.text = text
        } else {
            binding.tvResend.visibility = View.VISIBLE
            binding.countdown.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (isOtpView()) {
            countDownTimer?.cancel()
            binding.tvResend.visibility = View.GONE
            binding.countdown.visibility = View.GONE
            hideOtpView()
        } else
            super.onBackPressed()
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        super.onDestroy()
    }


    /* private fun showOption() {
         val sheet = MessageSheetBuilder()
             .setTitleText("Logout")
             .setMessageText("Are you sure want to logout?")
             .setPositiveButton("Ok")
             .setNegativeButton("cancel")
             .setListener(object : MessageSheetBuilder.Listener {
                 override fun onPositiveButtonClick(sheet: BottomSheetDialogFragment) {
                     sheet.dismissAllowingStateLoss()
                     sharedPrefManager.clearUser()
                     val i = Intent(this@MainActivity, LoginActivity::class.java)
                     startActivity(i)
                     showSuccessToast("Logout")
                     finishAffinity()
                 }


             }).build()
         showSheet(sheet)

     }*/
    private fun showSheet() {
        if (sheet1 == null) {
            var validField = false
            sheet1 = BaseCloseSheet(
                R.layout.sheet_add_company,
                object : BaseCloseSheet.ClickListener<SheetAddCompanyBinding> {
                    override fun onClick(view: View) {
                        if (view.id == R.id.btn_submit) {
                            if (validField) {
                                sheet1?.let {
                                    pincode = it.getBinding().etPincode.text.toString()
                                    name = it.getBinding().etname.text.toString()
                                    showOtpView()
                                    startTimer()
                                    it.dismissAllowingStateLoss()
                                }
                            } else {
                                showInfoToast("Pin code or Name not valid")
                            }
                        }
                    }

                    override fun onViewCreated(b: SheetAddCompanyBinding) {
                        b.etPincode.attachValidator(object : Validator {
                            override fun isValidField(s: String): Boolean {
                                validField = s.length == 6
                                return validField
                            }
                        })
                        b.etname.attachValidator(object : Validator {
                            override fun isValidField(s: String): Boolean {
                                validField = s.length > 5
                                return validField
                            }
                        })

                    }

                    override fun onClose() {
                        binding.blur.visibility = View.GONE
                    }
                }
            )
        }
        showSheetBlur(binding.main, binding.blur, sheet1)
    }
}