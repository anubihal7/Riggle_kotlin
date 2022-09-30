package com.rigle.servicehub.ui.inventory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.Inventory
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.*
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.RVAdapter
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InventoryFragment : BaseFragment<FragmentInventoryBinding>() {
    private lateinit var adapter: RVAdapter<Inventory, HolderInventoryBinding>
    private val viewmodel: InventoryViewModel by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_inventory
    }

    override fun getViewModel(): BaseViewModel = viewmodel


    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        initAdapter()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            when (v.id) {
                R.id.iv_back -> {
                    Navigation.findNavController(v).navigateUp()
                }
            }
        })

        viewmodel.obrInventory.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        if (adapter.itemCount == 0)
                            binding.loader.showLoading()
                    }
                    Status.SUCCESS -> {
                        binding.loader.hideLoading()
                        adapter.setDataList(resource.data)
                        updateEmptyView(adapter.itemCount, "No Stocks")
                    }
                    Status.ERROR -> {
                        binding.loader.hideLoading()
                        updateEmptyView(adapter.itemCount, resource.message)
                    }
                    Status.CACHED -> {

                    }
                }
            })
    }

    override fun onStart() {
        super.onStart()
        viewmodel.getInventory()
    }

    private fun updateEmptyView(itemCount: Int, message: String?) {
        if (itemCount > 0) {
            binding.vEmpty.hideEmptyView()
        } else {
            binding.vEmpty.showEmptyView(message)
        }
    }

    private fun initAdapter() {
        adapter = RVAdapter(R.layout.holder_inventory, BR.bean, object : RVAdapter.Callback<Inventory> {
            override fun onItemClick(v: View, m: Inventory) {
                 if (v.id == R.id.tv_detail) {
                      val bundle = Bundle()
                      bundle.putString("id", m.brand?.id.toString())
                      Navigation.findNavController(v).navigate(R.id.frg_detail_inventory, bundle)
                  }
            }
        })
        binding.rvOne.layoutManager = LinearLayoutManager(context)
        binding.rvOne.adapter = adapter

    }


    private fun initViews() {
        binding.toolbar.tvTitle.text = getString(R.string.inventory)
    }
}
