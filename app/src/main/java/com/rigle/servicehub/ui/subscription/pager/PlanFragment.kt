package com.rigle.servicehub.ui.subscription.pager

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.PlanResponse
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentPlanBinding
import com.rigle.servicehub.databinding.HolderPlanBinding
import com.rigle.servicehub.databinding.ViewPointBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.RVAdapter
import com.rigle.servicehub.ui.base.sheet.MessageSheetBuilder
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.openBrowser
import com.rigle.servicehub.utils.extension.showErrorToast
import com.rigle.servicehub.utils.extension.showSheet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class PlanFragment : BaseFragment<FragmentPlanBinding>() {
    private lateinit var adapter: RVAdapter<PlanResponse.SubscriptionData.Plan, HolderPlanBinding>
    private val viewmodel: PlanViewModel by viewModels()
    private var duration = 0
    override fun getLayoutResource(): Int {
        return R.layout.fragment_plan
    }

    companion object {
        fun newInstance(plan: PlanResponse.SubscriptionData): PlanFragment {
            val args = Bundle()
            args.putParcelable("data", plan)
            val fragment = PlanFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun getViewModel(): BaseViewModel = viewmodel

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            // attachEmptyViewNavigation(v)
        })
        viewmodel.obrBuy.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        openBrowser(resource.data?.paymentLink)
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


    private fun initViews() {
        arguments?.getParcelable<PlanResponse.SubscriptionData>("data")?.let { it ->
            it.durationId?.let {
                duration = it
            }
            binding.tvFeatureTitle.text = it.featureTitle
            binding.llOne.removeAllViews()
            it.features?.forEach {
                val b = DataBindingUtil.inflate<ViewPointBinding>(
                    layoutInflater,
                    R.layout.view_point,
                    binding.llOne,
                    false
                )
                b.tvOne.text = it
                binding.llOne.addView(b.root)
            }
            binding.rvOne.layoutManager = LinearLayoutManager(context)
            adapter = RVAdapter(
                R.layout.holder_plan,
                BR.bean,
                object : RVAdapter.Callback<PlanResponse.SubscriptionData.Plan> {
                    override fun onItemClick(v: View, m: PlanResponse.SubscriptionData.Plan) {
                        showMessage(m)
                    }
                })
            binding.rvOne.adapter = adapter
            adapter.setDataList(it.plans)
        }

    }


    private fun buyPlan(plan: PlanResponse.SubscriptionData.Plan) {
        val parm = JSONObject()
        parm.put("duration", duration.toString())
        parm.put("plan_type", plan.planId.toString())
        parm.put("company", sharedPrefManager.getCurrentUser()?.company?.id)
        viewmodel.buyPlan(sharedPrefManager.getCurrentUser()?.company?.id.toString(), parm)
    }

    private fun showMessage(plan: PlanResponse.SubscriptionData.Plan) {
        val sheet = MessageSheetBuilder()
            .setTitleText("Buy Plan")
            .setMessageText("Are you sure want to buy plan worth ${plan.monthlyAmount} ?")
            .setPositiveButton("Activate")
            .setNegativeButton("cancel")
            .setListener(object : MessageSheetBuilder.Listener {
                override fun onPositiveButtonClick(sheet: BottomSheetDialogFragment) {
                    sheet.dismissAllowingStateLoss()
                    buyPlan(plan)
                }

                override fun onNegativeButtonClick(sheet: BottomSheetDialogFragment) {
                    sheet.dismissAllowingStateLoss()
                }
            })
            .build()
        showSheet(sheet)

    }
}