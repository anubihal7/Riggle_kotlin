package com.rigle.servicehub.ui.business

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.Company
import com.rigle.servicehub.data.model.LocalMedia
import com.rigle.servicehub.data.model.RigleConstantsResponse
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentBusinessBinding
import com.rigle.servicehub.ui.account.edit.EditAccountFragment
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.info.OnBoardInfoActivity
import com.rigle.servicehub.utils.callbacks.Validator
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.attachValidator
import com.rigle.servicehub.utils.extension.showErrorToast
import com.rigle.servicehub.utils.extension.showInfoToast
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessFragment : BaseFragment<FragmentBusinessBinding>() {
    private val CODE_FILE = 10001
    private val viewmodel: BusinessViewModel by viewModels()
    val listType = arrayListOf<RigleConstantsResponse.CompanyProofType>()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_business
    }

    override fun getViewModel(): BaseViewModel = viewmodel

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        initSpinner()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            if (v.id == R.id.v_choose) {
                ImagePicker.with(this)
                    .crop()
                    .start(CODE_FILE)
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
                        if (activity is OnBoardInfoActivity) {
                            val s = activity as OnBoardInfoActivity
                            s.setCurrentPage(3)
                        } else {
                            val s = parentFragment as EditAccountFragment
                            s.backNavigate()
                        }
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {

                    }
                }
            })
    }

    private fun initViews() {
        sharedPrefManager.getCurrentUser()?.let { it1 ->
            it1.company?.let { it2 ->
                viewmodel.fieldName.set(it2.name)
                viewmodel.fieldAddress.set(it2.addressLine)
                viewmodel.fieldLocality.set(it2.locality)
                viewmodel.fieldPincode.set(it2.pincode)
                viewmodel.fieldCity.set(it2.city)
                viewmodel.fieldState.set(it2.state)
                it2.extra?.let { it5 ->
                    viewmodel.fieldProof.set(it5.proofFile)
                    viewmodel.fieldGst.set(it5.gstNumber)
                    binding.cbGst.isChecked = it5.isGstInvoice == true

                    sharedPrefManager.getRigleConstants()?.companyProofTypes?.forEach {
                        if (it.value.equals(it5.proofType)) {
                            binding.docType = it
                        }
                    }
                }

            }
        }

        disableViewState()
        binding.etFirmName.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
                return s.length > 5
            }
        })
        binding.etGstNo.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
                return s.length > 5
            }
        })
        binding.etStreet.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
                return s.length >= 3
            }
        })
        binding.etPin.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
                return s.length >= 6
            }
        })
        binding.etBuilding.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
                return s.length > 3
            }
        })
    }

    fun disableViewState() {
        binding.etPin.isEnabled = false
        binding.etCity.isEnabled = false
        binding.etState.isEnabled = false

        binding.etPin.alpha = .2f
        binding.etCity.alpha = .2f
        binding.etState.alpha = .2f
    }

    private fun initSpinner() {
        val list = arrayListOf<String>()
        sharedPrefManager.getRigleConstants()?.companyProofTypes?.let { it ->
            listType.addAll(it)
            it.forEach {
                list.add(it.name.toString())
            }
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, list)
        binding.etproof.adapter = adapter
        binding.etproof.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.docType = listType[position]
                binding.path = null
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }


    fun updateProfile() {
        when {
            viewmodel.fieldName.get().isNullOrBlank() -> {
                binding.etFirmName.error = "Invalid"
            }
            viewmodel.fieldGst.get().isNullOrBlank() -> {
                binding.etGstNo.error = "Invalid"
            }
            viewmodel.fieldAddress.get().isNullOrBlank() -> {
                binding.etStreet.error = "Invalid"
            }
            viewmodel.fieldLocality.get().isNullOrBlank() -> {
                binding.etBuilding.error = "Invalid"
            }
            viewmodel.fieldPincode.get().isNullOrBlank() -> {
                binding.etPin.error = "Invalid"
            }
            viewmodel.fieldCity.get().isNullOrBlank() -> {
                binding.etCity.error = "Invalid"
            }
            viewmodel.fieldState.get().isNullOrBlank() -> {
                binding.etState.error = "Invalid"
            }
            binding.path.isNullOrBlank() -> {
                showInfoToast("Choose Document file")
            }

            else -> {
                val parm = hashMapOf<String, String>()
                parm["name"] = viewmodel.fieldName.get().toString()
                parm["gst_number"] = viewmodel.fieldGst.get().toString()
                parm["is_gst_invoice"] = binding.cbGst.isChecked.toString()
                parm["proof_type"] = binding.docType!!.value.toString()
                parm["address_line"] = viewmodel.fieldAddress.get().toString()
                parm["locality"] = viewmodel.fieldLocality.get().toString()
                parm["pincode"] = viewmodel.fieldPincode.get().toString()
                parm["city"] = viewmodel.fieldAddress.get().toString()
                parm["state"] = viewmodel.fieldAddress.get().toString()
                val list = arrayListOf<LocalMedia>()
                list.add(LocalMedia("proof_file", binding.path.toString(), null))
                viewmodel.updateBusiness(parm, list)
            }
        }
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CODE_FILE -> {
                    data?.data?.path?.let {
                        binding.path = it
                    }
                }

            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            showErrorToast(ImagePicker.getError(data))
        }
    }
}

