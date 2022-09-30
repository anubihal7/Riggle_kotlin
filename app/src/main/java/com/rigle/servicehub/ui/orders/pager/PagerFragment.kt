package com.rigle.servicehub.ui.orders.pager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.OrderBean
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentPagerOrderBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.MultiAdapter
import com.rigle.servicehub.ui.orders.detail.DetailOrderActivity
import com.rigle.servicehub.utils.Constants
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.*
import com.google.gson.JsonElement
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class PagerFragment : BaseFragment<FragmentPagerOrderBinding>() {
    private lateinit var layouManger: LinearLayoutManager
    private lateinit var adapter: MultiAdapter<OrderBean>
    private val viewmodel: PagerViewModel by viewModels()
    private lateinit var type: String
    private var page = 1
    private var pageSize = 100
    private var lastPosition = 0
    override fun getLayoutResource(): Int {
        return R.layout.fragment_pager_order
    }

    companion object {
        fun newInstance(type: String): PagerFragment {
            val args = Bundle()
            args.putString("type", type)
            val fragment = PagerFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun getViewModel(): BaseViewModel = viewmodel

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            attachEmptyViewNavigation(v)
        })
        viewmodel.obrData.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        if (adapter.itemCount == 0)
                            binding.loader.showLoading()
                    }
                    Status.SUCCESS -> {
                        binding.loader.hideLoading()
                        adapter.setDataList(resource.data?.results)
                        updateEmptyView(adapter.itemCount, "No record found")
                        scrollView()
                    }
                    Status.ERROR -> {
                        binding.loader.hideLoading()
                        updateEmptyView(adapter.itemCount, resource.message)
                    }
                    Status.CACHED -> {
                        resource.data?.results?.let {
                            binding.loader.hideLoading()
                            adapter.setDataList(it)
                            updateEmptyView(adapter.itemCount, "No record found")
                            scrollView()
                        }

                    }
                }
            })
        viewmodel.obrRunner.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                    }
                    Status.ERROR -> {
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {

                    }
                }
            })
        viewmodel.obrAssignRunner.observe(
            this,
            SingleRequestEvent.RequestObserver<JsonElement> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showSuccessToast(resource.message)
                        getData()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {

                    }
                }
            })

        viewmodel.obrConfirm.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showSuccessToast(resource.message)
                        getData()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        getData()
                    }
                    Status.CACHED -> {

                    }
                }
            })
    }

    private fun scrollView() {
        try {
            if (lastPosition >= 0 && lastPosition < adapter.itemCount) {
                binding.rvOne.smoothScrollToPosition(lastPosition)
            }
        } catch (e: Exception) {
        }
    }

    private fun showRunnerPopup(v: View, orderBean: OrderBean) {
        viewmodel.obrRunner.value?.data?.results?.let { it ->
            val popupMenu = PopupMenu(context, v)
            it.forEach {
                if (it.isActive == true) {
                    val s = popupMenu.menu.add(it.fullName)
                    val intent = Intent()
                    intent.putExtra("id", it.id.toString())
                    s.intent = intent
                }
            }
            popupMenu.setOnMenuItemClickListener { item ->
                val parm = JSONObject()
                var runnerId = ""
                item?.intent?.getStringExtra("id")?.let {
                    parm.put("runner", it)
                    runnerId = it
                }
                if (type == Constants.STATUS_PENDING) {
                    viewmodel.runnerConfirm(orderBean.id.toString(), runnerId)
                } else {
                    viewmodel.assignRunner(orderBean.id.toString(), parm)
                }
                true
            }
            popupMenu.show()

        }
    }

    private fun updateEmptyView(itemCount: Int, message: String?) {
        if (itemCount > 0) {
            binding.empty.hideEmptyView()
        } else {
            binding.empty.showEmptyView(message)
        }
    }

    override fun onResume() {
        super.onResume()
        viewmodel.getRunner()
        getData()
    }

    private fun getData() {
        val map = hashMapOf<String, String>()
        map["tab_name"] = type
        map["page_size"] = pageSize.toString()
        map["page"] = page.toString()
        map["expand"] = "buyer.admin,assigned_runner,statuses"
        viewmodel.getData(map)
    }

    private fun initViews() {
        Log.e("Pager", "pos" + lastPosition)
        arguments?.let {
            type = it.getString("type").toString()
        }
        layouManger = LinearLayoutManager(context)
        binding.rvOne.layoutManager = layouManger


        adapter = object : MultiAdapter<OrderBean>(object : Callback<OrderBean> {
            override fun onItemClick(v: View, m: OrderBean) {
                if (v.id == R.id.tv_assign) {
                    showRunnerPopup(v, m)
                } else if (v.id == R.id.tv_reassign) {
                    showRunnerPopup(v, m)
                } else if (v.id == R.id.tv_confirm) {
                    // viewmodel.runnerConfirm(m.id.toString(), m.assignedRunner?.id.toString())
                }
            }

            override fun onItemClick(v: View, m: OrderBean, position: Int) {
                if (v.id == R.id.tv_detail) {
                    showDetailView(v, m, position)
                }
            }
        }) {
            override fun getLayoutResource(viewType: Int): Int {
                return when (viewType) {
                    1 -> R.layout.holder_pending_order
                    2 -> R.layout.holder_confirm_order
                    3 -> R.layout.holder_completed_order
                    4 -> R.layout.holder_cancel_order
                    5 -> R.layout.holder_outstanding_order
                    else -> R.layout.holder_pending_order
                }
            }

            override fun getViewType(bean: OrderBean): Int {
                return when (type) {
                    Constants.STATUS_PENDING -> 1
                    Constants.STATUS_CONFIRMED -> 2
                    Constants.STATUS_COMPLETED -> 3
                    Constants.STATUS_CANCELLED -> 4
                    Constants.STATUS_OUTSTANDING -> 5
                    else -> 0
                }
            }

        }
        //adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT
        binding.rvOne.adapter = adapter

    }

    private fun showDetailView(v: View, m: OrderBean, pos: Int) {
        lastPosition = pos
        Log.e("Pager", "pos open" + lastPosition)

        startActivity(Intent(context, DetailOrderActivity::class.java).apply {
            putExtra("orderId", m.id.toString())
        })
    }


}