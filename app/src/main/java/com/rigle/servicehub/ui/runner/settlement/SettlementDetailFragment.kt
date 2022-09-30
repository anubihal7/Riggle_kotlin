package com.rigle.servicehub.ui.runner.settlement

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
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettlementDetailFragment : BaseFragment<FragmentSettlementDetailBinding>() {
    private val viewmodel: SettlementDetailViewModel by viewModels()

    override fun getLayoutResource(): Int {
        return R.layout.fragment_settlement_detail
    }

    override fun getViewModel(): BaseViewModel = viewmodel

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            if (v.id == R.id.iv_back) {
                Navigation.findNavController(v).navigateUp()
            }
        })
        viewmodel.obrRunner.observe(
            this,
            SingleRequestEvent.RequestObserver<Runner> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        binding.loader.showLoading()
                    }
                    Status.SUCCESS -> {
                        binding.loader.hideLoading()
                        binding.bean = resource.data
                    }
                    Status.ERROR -> {
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {
                    }
                }
            })
    }

    override fun onStart() {
        super.onStart()
        arguments?.getString("id")?.let {
            viewmodel.getRunner(it)
        }
    }

    private fun initViews() {
        binding.toolbar.tvTitle.text = getString(R.string.runner_detail)
    }

}