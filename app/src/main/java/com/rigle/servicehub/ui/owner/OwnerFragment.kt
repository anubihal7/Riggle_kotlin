package com.rigle.servicehub.ui.owner

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.Company
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentOwnerBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.info.OnBoardInfoActivity
import com.rigle.servicehub.utils.callbacks.Validator
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.attachValidator
import com.rigle.servicehub.utils.extension.showErrorToast
import com.rigle.servicehub.utils.extension.showSuccessToast
import com.google.gson.JsonElement
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.lang.StringBuilder

@AndroidEntryPoint
class OwnerFragment : BaseFragment<FragmentOwnerBinding>() {
    private val viewmodel: OwnerViewModel by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_owner
    }

    override fun getViewModel(): BaseViewModel = viewmodel
    override fun onResume() {
        super.onResume()
        viewmodel.field_fullname.set(sharedPrefManager.getCurrentUser()?.fullName)
    }

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initSpinner()
        initViews()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            if (v.id == R.id.ll_add_co_owner) {
                addCoOwner()
            }
        })
        viewmodel.obrOwner.observe(
            this,
            SingleRequestEvent.RequestObserver<JsonElement> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showSuccessToast(resource.message)
                        viewmodel.field_co_name.set(null)
                        viewmodel.field_mobile.set(null)
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {

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
                        if (activity is OnBoardInfoActivity) {
                            val s = activity as OnBoardInfoActivity
                            s.setCurrentPage(2)
                        } else {
                            showSuccessToast(resource.message)
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
        binding.etFirmName.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
               return s.length > 5
            }
        })
        binding.etCoowner.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
               return s.length > 5
            }
        })
        binding.etPhone.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
               return s.length >= 10
            }
        })
    }

    private fun initSpinner() {
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.other_docs, R.layout.simple_spinner_item
        )
        // binding.spother.adapter = adapter
    }


    fun updateProfile() {
        when {
            viewmodel.field_fullname.get().isNullOrBlank() -> {
                binding.etFirmName.error = "Invalid"
            }
            else -> {
                viewmodel.updateUser(viewmodel.field_fullname.get().toString())
            }
        }
    }

    fun addCoOwner() {
        if (viewmodel.field_co_name.get().isNullOrBlank()) {
            binding.etCoowner.error = "Invalid"
        } else if (viewmodel.field_mobile.get().isNullOrBlank()) {
            binding.etPhone.error = "Invalid"
        } else if (viewmodel.field_mobile.get()?.length!! < 10) {
            binding.etPhone.error = "Invalid"
        } else {
            val jsonObject = JSONObject()
            val words =
                viewmodel.field_co_name.get().toString().split("\\s".toRegex()).toTypedArray()
            jsonObject.put("first_name", words[0])
            val sl = StringBuilder("")
            words.forEachIndexed { index, s ->
                if (index > 0) {
                    sl.append(s)
                    sl.append(" ")
                }
            }
            jsonObject.put("last_name", sl.toString())
            jsonObject.put("mobile", viewmodel.field_mobile.get().toString())
            jsonObject.put("role", "company_admin")
            jsonObject.put("company", sharedPrefManager.getCurrentUser()?.company?.id)
            viewmodel.addUser(jsonObject)
        }
    }
}


