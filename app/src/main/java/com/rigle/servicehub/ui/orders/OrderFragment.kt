package com.rigle.servicehub.ui.orders

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.data.model.helper.UploadBean
import com.rigle.servicehub.databinding.FragmentOrderBinding
import com.rigle.servicehub.databinding.ViewTabBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.orders.pager.PagerAdapter
import com.rigle.servicehub.ui.orders.pager.PagerFragment
import com.rigle.servicehub.utils.Constants
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.showErrorToast
import com.rigle.servicehub.utils.extension.showSuccessToast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding>() {
    private val viewmodel: OrderViewModel by viewModels()

    override fun getLayoutResource(): Int {
        return R.layout.fragment_order
    }

    override fun getViewModel(): BaseViewModel = viewmodel

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        initPager()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            if (v.id == R.id.iv_back) {
                Navigation.findNavController(v).navigateUp()
            }
        })
        viewmodel.obrUpdate.observe(
            this,
            SingleRequestEvent.RequestObserver<UploadBean> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showSuccessToast(resource.message)

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

    private fun initPager() {
        addOnTabSelectedListener()
        val name = arrayOf("Pending", "Outstanding", "Confirmed", "Completed", "Cancelled")
        val list = arrayListOf<Fragment>()
        list.add(PagerFragment.newInstance(Constants.STATUS_PENDING))
        list.add(PagerFragment.newInstance(Constants.STATUS_OUTSTANDING))
        list.add(PagerFragment.newInstance(Constants.STATUS_CONFIRMED))
        list.add(PagerFragment.newInstance(Constants.STATUS_COMPLETED))
        list.add(PagerFragment.newInstance(Constants.STATUS_CANCELLED))
        binding.pager.isUserInputEnabled = false
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
                /* if (checked)
                     it.tvName.animate().scaleX(1.2f).scaleY(1.2f)
                 else
                     it.tvName.animate().scaleX(1f).scaleY(1f)*/
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

    }

    private fun initViews() {
        binding.toolbar.tvTitle.text = getString(R.string.ret_order)
    }

}