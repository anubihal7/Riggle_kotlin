package com.rigle.servicehub.ui.subscription

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.PlanResponse
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentSubscriptionBinding
import com.rigle.servicehub.databinding.ViewTabBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.orders.pager.PagerAdapter
import com.rigle.servicehub.ui.subscription.pager.PlanFragment
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.hideLoading
import com.rigle.servicehub.utils.extension.showErrorToast
import com.rigle.servicehub.utils.extension.showLoading
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionFragment : BaseFragment<FragmentSubscriptionBinding>() {
    private val viewmodel: SubscriptionViewModel by viewModels()

    override fun getLayoutResource(): Int {
        return R.layout.fragment_subscription
    }

    override fun getViewModel(): BaseViewModel = viewmodel

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            if (v.id == R.id.iv_back) {
                Navigation.findNavController(v).navigateUp()
            }
        })
        viewmodel.obrPlan.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        binding.loader.showLoading()
                    }
                    Status.SUCCESS -> {
                        binding.loader.hideLoading()
                        initPager(resource.data)
                    }
                    Status.ERROR -> {
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {

                    }
                }
            })
    }

    private fun initPager(data: PlanResponse?) {
        addOnTabSelectedListener()
        val name = arrayListOf<String>()
        val list = arrayListOf<Fragment>()
        data?.subscriptionData?.forEach {
            name.add(it.duration.toString())
            list.add(PlanFragment.newInstance(it))
        }
        val pagerAdapter = PagerAdapter(this, list)
        binding.pager.adapter = pagerAdapter
        TabLayoutMediator(binding.tab, binding.pager) { tab, position ->
            tab.text = name[position]
        }.attach()
        addViews()
    }

    private fun addOnTabSelectedListener() {
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                updateTab(tab, true)

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                updateTab(tab, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun updateTab(tab: TabLayout.Tab?, checked: Boolean) {
        val view = tab?.customView
        if (view != null)
            DataBindingUtil.getBinding<ViewTabBinding>(view)?.let {
                it.checked = checked
            }
    }


    private fun addViews() {
        for (i in 0 until binding.tab.tabCount) {
            val tab = binding.tab.getTabAt(i)
            if (tab != null) {
                val b = DataBindingUtil.inflate<ViewTabBinding>(
                    layoutInflater,
                    R.layout.view_tab,
                    null,
                    false
                )
                b.checked = i == 0
                b.title = tab.text.toString()
                tab.customView = b.root
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewmodel.getPlans()
    }

    private fun initViews() {
        binding.toolbar.tvTitle.text = getString(R.string.subscription)
    }

}