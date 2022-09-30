package com.rigle.servicehub.ui.runner.update

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.Runner
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.*
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.callbacks.Validator
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.*
import com.google.gson.JsonElement
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class UpdateRunnerFragment : BaseFragment<FragmentRunnerUpdateBinding>() {
    private var runnerId: Int? = null
    private val viewmodel: UpdateRunnerViewModel by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_runner_update
    }

    override fun getViewModel(): BaseViewModel = viewmodel


    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            if (v.id == R.id.ll_add) {
                addRunner()
            }else if (v.id == R.id.iv_back) {
                Navigation.findNavController(v).navigateUp()
                }
        })
        viewmodel.obrUpdate.observe(
            this,
            SingleRequestEvent.RequestObserver<JsonElement> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showSuccessToast(resource.message)
                        viewmodel.fieldName.set(null)
                        viewmodel.fieldPhone.set(null)
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

    private fun addRunner() {
        when {
            viewmodel.fieldName.get().isNullOrBlank() -> {
                binding.etname.error = "Invalid"
            }
            viewmodel.fieldPhone.get().isNullOrBlank() -> {
                binding.etPhone.error = "Invalid"
            }
            else -> {
                val name = viewmodel.fieldName.get()?.split(" ")
                val parm = JSONObject()
                name?.let {
                    if (it.isNotEmpty())
                        parm.put("first_name", it[0])
                    if (it.size > 1)
                        parm.put("last_name", it[1])
                    else
                        parm.put("last_name", "")
                }
                parm.put("mobile", viewmodel.fieldPhone.get().toString())
                parm.put("role", "runner")
                parm.put("company", sharedPrefManager.getCurrentUser()?.company?.id)
                viewmodel.updateRunner(runnerId.toString(), parm)
            }
        }
    }


    private fun initViews() {
        binding.toolbar.tvTitle.text = getString(R.string.edit_runner_info)
        binding.etname.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
                return s.length > 3
            }
        })
        binding.etPhone.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
                return s.length >= 10
            }
        })
        arguments?.getParcelable<Runner>("runner")?.let {
            runnerId = it.id
            viewmodel.fieldName.set(it.fullName)
            viewmodel.fieldPhone.set(it.mobile)
        }
    }
}