package com.rigle.servicehub.ui.settle

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.view.ContextThemeWrapper
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.SettleBeanItem
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.*
import com.rigle.servicehub.ui.base.BaseActivity
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.BaseAdapter
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettlementActivity : BaseActivity<ActivitySettlementBinding>() {

    val viewModel: SettlementActivityVM by viewModels()
    lateinit var adapter: BaseAdapter<SettleBeanItem, HolderSettleBinding>

    companion object {
        fun newIntent(activity: Context): Intent {
            val intent = Intent(activity, SettlementActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_settlement
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        binding.toolbar.tvTitle.text = "Settlements"
        initAdapter()
        viewModel.onClick.observe(this) {
            when (it?.id) {
                R.id.iv_back -> {
                    finish()
                }
            }
        }
        viewModel.obrData.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        binding.loader.showLoading()
                    }
                    Status.SUCCESS -> {
                        binding.loader.hideLoading()
                        adapter.setDataList(resource.data)
                        updateEmptyView(adapter.itemCount, "No record found")
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

    private fun updateEmptyView(itemCount: Int, message: String?) {
        if (itemCount > 0) {
            binding.empty.hideEmptyView()
        } else {
            binding.empty.showEmptyView(message)
        }
    }

    private fun initAdapter() {
        adapter = object : BaseAdapter<SettleBeanItem, HolderSettleBinding>(
            R.layout.holder_settle,
            object : Callback<SettleBeanItem> {
                override fun onItemClick(v: View, m: SettleBeanItem) {
                    showPopupMenu(v, m)
                }
            }) {
            override fun onBind(
                binding: HolderSettleBinding,
                bean: SettleBeanItem,
                position: Int
            ) {
                binding.line.removeAllViews()
                var trips = 0
                bean.settlementData?.settlements?.forEach {
                    val b = DataBindingUtil.inflate<ViewData1Binding>(
                        layoutInflater,
                        R.layout.view_data1,
                        binding.line,
                        false
                    )
                    b.bean = it
                    it.count?.let { it1 ->
                        trips += it1
                    }

                    binding.line.addView(b.root)
                }
                binding.trips = trips
                bean.settlementData?.let {
                    binding.ordervalue = it.orderValue?.toFloat()
                }
            }

        }
        binding.rvOne.layoutManager = LinearLayoutManager(this)
        binding.rvOne.adapter = adapter
    }

    private fun showPopupMenu(v: View, m: SettleBeanItem) {
        val wrapper = ContextThemeWrapper(this, R.style.BasePopupMenu)
        val popupMenu = PopupMenu(wrapper, v)
        popupMenu.menu.add("Settle Amount")
        popupMenu.setOnMenuItemClickListener {
            true
        }
        popupMenu.show()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getData()
    }

}