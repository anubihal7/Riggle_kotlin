package com.rigle.servicehub.ui.sales.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.SalesPerson
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentSalesDetailBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.hideLoading
import com.rigle.servicehub.utils.extension.showErrorToast
import com.rigle.servicehub.utils.extension.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SalesDetailFragment : BaseFragment<FragmentSalesDetailBinding>() {
    private val viewmodel: SalesDetailViewModel by viewModels()

    override fun getLayoutResource(): Int {
        return R.layout.fragment_sales_detail
    }

    override fun getViewModel(): BaseViewModel = viewmodel

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            if (v.id == R.id.iv_back) {
                Navigation.findNavController(v).navigateUp()
            } else if (v.id == R.id.tv_detail) {
                binding.bean?.let {
                    val bundle = Bundle()
                    bundle.putString("id", it.id.toString())
                    Navigation.findNavController(v).navigate(R.id.frg_sales, bundle)
                }
            } else if (v.id == R.id.tv_disable) {
                binding.bean?.let {
                    val active = it.isActive!!
                    viewmodel.updateUser(it.id.toString(), !active)
                }

            }
        })
        viewmodel.obrSalesPerson.observe(
            this,
            SingleRequestEvent.RequestObserver<SalesPerson> { resource ->
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

        viewmodel.obrUpdate.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        arguments?.getString("id")?.let {
                            viewmodel.getSalesPerson(it)
                        }
                    }
                    Status.ERROR -> {
                        hideLoading()
                        arguments?.getString("id")?.let {
                            viewmodel.getSalesPerson(it)
                        }
                    }
                    Status.CACHED -> {

                    }
                }
            })
    }

    override fun onStart() {
        super.onStart()
        arguments?.getString("id")?.let {
            viewmodel.getSalesPerson(it)
        }
    }

    private fun initViews() {
        binding.toolbar.tvTitle.text = getString(R.string.sales_person)
    }

}