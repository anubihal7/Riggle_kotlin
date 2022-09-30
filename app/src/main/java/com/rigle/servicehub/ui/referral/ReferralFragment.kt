package com.rigle.servicehub.ui.referral

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.OfferBean
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.data.network.base.PageResponse
import com.rigle.servicehub.databinding.*
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.RVAdapter
import com.rigle.servicehub.ui.coupon.AddCouponActivity
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferralFragment : BaseFragment<FragmentReferralBinding>() {
    private val viewmodel: ReferralViewModel by viewModels()
    private lateinit var adapter: RVAdapter<OfferBean, HolderOfferBinding>
    override fun getLayoutResource(): Int {
        return R.layout.fragment_referral
    }

    override fun getViewModel(): BaseViewModel = viewmodel

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            if (v.id == R.id.iv_back) {
                Navigation.findNavController(v).navigateUp()
            } else if (v.id == R.id.btn_activate) {
                startActivity(AddCouponActivity.newIntent(requireActivity()))
            }
        })
        viewmodel.obrOffers.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        binding.loader.showLoading()
                    }
                    Status.SUCCESS -> {
                        updateUi(resource.data)
                        binding.loader.hideLoading()
                    }
                    Status.ERROR -> {
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {
                        updateUi(resource.data)
                        binding.loader.hideLoading()
                    }
                }
            })
    }

    override fun onStart() {
        super.onStart()
        viewmodel.getOffers()
    }

    private fun updateUi(data: PageResponse<OfferBean>?) {
        adapter.setDataList(data?.results)
        if (data?.results != null && data.results.isNotEmpty()) {
            binding.rlOffers.visibility = View.VISIBLE
            binding.rlNone.visibility = View.GONE
        } else {
            binding.rlOffers.visibility = View.GONE
            binding.rlNone.visibility = View.VISIBLE
        }

    }

    private fun initViews() {
        binding.toolbar.tvTitle.text = getString(R.string.coupon_codes)
        adapter = RVAdapter(R.layout.holder_offer, BR.bean, object : RVAdapter.Callback<OfferBean> {
            override fun onItemClick(v: View, m: OfferBean) {

            }
        })
        binding.rlOffers.layoutManager = LinearLayoutManager(context)
        binding.rlOffers.adapter = adapter

    }


}