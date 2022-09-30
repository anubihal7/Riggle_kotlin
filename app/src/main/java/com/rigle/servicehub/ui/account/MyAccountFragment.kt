package com.rigle.servicehub.ui.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.Company
import com.rigle.servicehub.data.model.MyAccountResponse
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.*
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.scanner.ScannerBarcodeActivity
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.hideLoading
import com.rigle.servicehub.utils.extension.showErrorToast
import com.rigle.servicehub.utils.extension.showLoading
import com.rigle.servicehub.utils.extension.showSuccessToast
import com.google.zxing.WriterException
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


@AndroidEntryPoint
class MyAccountFragment : BaseFragment<FragmentAccountBinding>() {
    private val viewmodel: MyAccountViewModel by viewModels()
    private var qrcode: String? = null
    override fun getLayoutResource(): Int {
        return R.layout.fragment_account
    }

    override fun getViewModel(): BaseViewModel = viewmodel

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            when (v.id) {
                R.id.iv_back -> {
                    Navigation.findNavController(v).navigateUp()
                }
                R.id.ll_edit_owner -> {
                    Navigation.findNavController(v).navigate(
                        R.id.frg_edit_account, bundleOf(
                            Pair("title", "Owner Info"),
                            Pair("type", 1)
                        )
                    )
                }
                R.id.ll_edit_business -> {
                    Navigation.findNavController(v).navigate(
                        R.id.frg_edit_account,
                        bundleOf(Pair("title", "Business Info"), Pair("type", 2))
                    )
                }
                R.id.ll_add_qr -> {
                    launcher.launch(Intent(activity, ScannerBarcodeActivity::class.java))
                }
            }
        })
        viewmodel.obrMyAccount.observe(
            this,
            SingleRequestEvent.RequestObserver<MyAccountResponse> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        binding.loader.showLoading()
                    }
                    Status.SUCCESS -> {
                        binding.loader.hideLoading()
                        updateUi(resource.data)
                    }
                    Status.ERROR -> {
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {
                        updateUi(resource.data)
                    }
                }
            })
        viewmodel.obrUpdate.observe(
            this,
            SingleRequestEvent.RequestObserver<Company> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showSuccessToast(resource.message)
                        updateQrcode()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showErrorToast(resource.message)
                        qrcode = null
                        updateQrcode()
                    }
                    Status.CACHED -> {

                    }
                }
            })
    }

    private fun updateUi(data: MyAccountResponse?) {
        data?.let { it ->
            val myId = sharedPrefManager.getCurrentUser()?.id
            binding.llOne.removeAllViews()
            it.users?.forEach {
                if(it.role.equals("company_admin")) {
                    val b = DataBindingUtil.inflate<ViewItem1Binding>(
                        layoutInflater,
                        R.layout.view_item1,
                        binding.llOne, false
                    )
                    if (myId == it.id) {
                        b.tvName.text = it.fullName + "(You)"
                    } else
                        b.tvName.text = it.fullName
                    b.tvPhone.text = it.mobile
                    binding.llOne.addView(b.root)
                }
            }


            binding.llTwo.removeAllViews()
            binding.llTwo.addView(getView2Binding().apply {
                this.tvHeader.text = "Business Name"
                this.tvName.text = it.name
            }.root)

            binding.llTwo.addView(getView2Binding().apply {
                this.tvHeader.text = "GST Number"
                this.tvName.text = it.extra?.gstNumber
            }.root)

            /*   binding.llTwo.addView(getView2Binding().apply {
                   this.tvHeader.text = "GST Number"
                   this.tvName.text = it.extra?.gstNumber
               }.root)*/
            binding.llTwo.addView(getView2Binding().apply {
                this.tvHeader.text = "Address"
                this.tvName.text = it.fullAddress
            }.root)

            it.extra?.let {
                qrcode = it.qrCodeText
                updateQrcode()
            }
        }
    }

    private fun getView2Binding(): ViewItem2Binding {
        return DataBindingUtil.inflate<ViewItem2Binding>(
            layoutInflater,
            R.layout.view_item2,
            binding.llTwo, false
        )
    }


    override fun onStart() {
        super.onStart()
        viewmodel.getMyAccount()
    }


    private fun initViews() {
        binding.toolbar.tvTitle.text = getString(R.string.my_account)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getStringExtra("code")?.let {
                    qrcode = it
                    val parm = JSONObject()
                    parm.put("qr_code_text", qrcode.toString())
                    viewmodel.updateQrcode(parm)
                }
            }

        }

    private fun updateQrcode() {
        if (qrcode.isNullOrBlank()) {
            binding.ivQr.visibility = View.GONE
        } else {
            val qrgEncoder = QRGEncoder(qrcode, null, QRGContents.Type.TEXT, binding.fl1.width)
            try {
                binding.ivQr.setImageBitmap(qrgEncoder.encodeAsBitmap())
                binding.ivQr.visibility = View.VISIBLE
            } catch (e: WriterException) {
                showErrorToast(e.message.toString())
            }
        }
    }

}