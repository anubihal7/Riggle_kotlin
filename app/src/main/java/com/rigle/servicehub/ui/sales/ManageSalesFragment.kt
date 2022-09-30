package com.rigle.servicehub.ui.sales

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.SalesPerson
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentManageSalesBinding
import com.rigle.servicehub.databinding.HolderSalePersonBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.RVAdapter
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.hideEmptyView
import com.rigle.servicehub.utils.extension.hideLoading
import com.rigle.servicehub.utils.extension.showEmptyView
import com.rigle.servicehub.utils.extension.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageSalesFragment : BaseFragment<FragmentManageSalesBinding>() {
    private lateinit var adapter: RVAdapter<SalesPerson, HolderSalePersonBinding>
    private val viewmodel: MangeSalesViewModel by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_manage_sales
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
                R.id.btn_go,
                R.id.btn_add -> {
                    Navigation.findNavController(v).navigate(R.id.frg_sales)
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
                        viewmodel.getSalesPerson()
                    }
                    Status.ERROR -> {
                        hideLoading()
                    }
                    Status.CACHED -> {

                    }
                }
            })
        viewmodel.obrSalesPerson.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        binding.loader.showLoading()
                    }
                    Status.SUCCESS -> {
                        binding.loader.hideLoading()
                        adapter.setDataList(resource.data?.results)
                        updateEmptyView(adapter.itemCount, "No runner found")
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
        viewmodel.getSalesPerson()
    }

    private fun updateEmptyView(itemCount: Int, message: String?) {
        if (itemCount > 0) {
            if (itemCount > 1)
                binding.tvCount.text = "You have $itemCount Sales Persons"
            else
                binding.tvCount.text = "You have $itemCount Sales Person"
            binding.tvCount.visibility = View.VISIBLE
            binding.empty.hideEmptyView()
            binding.btnAdd.visibility = View.VISIBLE
        } else {
            binding.empty.showEmptyView(message)
            binding.btnAdd.visibility = View.GONE
            binding.tvCount.visibility = View.GONE
        }
    }

    private fun initAdapter() {
        adapter = RVAdapter(
            R.layout.holder_sale_person,
            BR.bean,
            object : RVAdapter.Callback<SalesPerson> {
                override fun onItemClick(v: View, m: SalesPerson) {
                    if (v.id == R.id.tv_detail) {
                        val bundle = Bundle()
                        bundle.putString("id", m.id.toString())
                        Navigation.findNavController(v).navigate(R.id.frg_detail_sales, bundle)
                    } else if (v.id == R.id.tv_edit) {
                        val bundle = Bundle()
                        bundle.putString("id", m.id.toString())
                        Navigation.findNavController(v).navigate(R.id.frg_sales, bundle)

                    } else if (v.id == R.id.tv_disable) {
                        m?.let {
                            val active = it.isActive!!
                            viewmodel.updateUser(it.id.toString(), !active)
                        }

                    }
                }
            })
        binding.rvOne.layoutManager = LinearLayoutManager(context)
        binding.rvOne.adapter = adapter

    }


    private fun initViews() {
        binding.empty.btnGo.text = getString(R.string.add_sales_person)
        binding.toolbar.tvTitle.text = getString(R.string.sales_person)
    }
}
