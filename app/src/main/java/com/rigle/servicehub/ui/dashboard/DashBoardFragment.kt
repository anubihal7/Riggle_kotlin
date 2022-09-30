package com.rigle.servicehub.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.hadiidbouk.charts.BarData
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.Company
import com.rigle.servicehub.data.model.DashboardResponse
import com.rigle.servicehub.data.model.PlanResponse
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentDashboardBinding
import com.rigle.servicehub.databinding.SheetIntroBinding
import com.rigle.servicehub.databinding.ViewCircleGraphBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.RVAdapter
import com.rigle.servicehub.ui.base.sheet.BaseCloseSheet
import com.rigle.servicehub.ui.settle.SettlementActivity
import com.rigle.servicehub.utils.UtilMethods
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.hideLoading
import com.rigle.servicehub.utils.extension.showErrorToast
import com.rigle.servicehub.utils.extension.showLoading
import com.rigle.servicehub.utils.extension.showSheetBlur
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class DashBoardFragment : BaseFragment<FragmentDashboardBinding>() {
    private var revenues: DashboardResponse.Revenues? = null
    private var sheet1: BaseCloseSheet<SheetIntroBinding>? = null
    private val viewmodel: DashBoardViewModel by viewModels()
    private lateinit var adapter: RVAdapter<DashBean, ViewCircleGraphBinding>
    private val daily = "Daily"
    private val weekly = "Weekly"
    private val monthly = "Monthly"
    private val yearly = "Yearly"
    private var currentType = daily
    override fun getLayoutResource(): Int {
        return R.layout.fragment_dashboard
    }

    override fun getViewModel(): BaseViewModel = viewmodel
    override fun getHorizontalBar(): LinearProgressIndicator? {
        return binding.toolbar.pbOne
    }

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            if (v.id == R.id.iv_settle) {
                val intent = SettlementActivity.newIntent(requireContext())
                startActivity(intent)
            } else if (v.id == R.id.btn_activate) {
                viewmodel.getPlans()
            } else if (v.id == R.id.tv_type) {
                showOptionType(v)
            }
        })
        viewmodel.obrPlan.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        processLinks(resource.data)

                    }
                    Status.ERROR -> {
                        hideLoading()
                    }
                    Status.CACHED -> {

                    }
                }
            })
        viewmodel.obrCompany.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        updateUserInfo(resource.data)
                    }
                    Status.ERROR -> {
                    }
                    Status.CACHED -> {
                        updateUserInfo(resource.data)
                    }
                }
            })

        viewmodel.obrDashboard.observe(this, SingleRequestEvent.RequestObserver { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    if (adapter.itemCount == 0)
                        binding.loader.showLoading()
                }
                Status.SUCCESS -> {
                    binding.loader.hideLoading()
                    revenues = resource.data?.revenues
                    currentType = daily
                    updateView(resource.data)
                    updateTypeView()
                }
                Status.ERROR -> {
                    binding.loader.hideLoading()
                    showErrorToast(resource.message)
                }
                Status.CACHED -> {
                    updateView(resource.data)
                }

            }
        })

    }

    private fun showOptionType(v: View) {
        val popupMenu = PopupMenu(requireContext(), v)
        popupMenu.menu.add(daily)
        popupMenu.menu.add(weekly)
        popupMenu.menu.add(monthly)
        popupMenu.menu.add(yearly)
        popupMenu.setOnMenuItemClickListener { item ->
            currentType = item?.title.toString()
            updateTypeView()
            true
        }
        popupMenu.show()
    }

    private fun updateTypeView() {
        revenues?.let {
            binding.tvType.text = currentType
            if (currentType == daily) {
                addGraph(it.daily)
            } else if (currentType == weekly) {
                addGraph(it.weekly)
            } else if (currentType == monthly) {
                addGraph(it.monthly)
            } else if (currentType == yearly) {
                addGraph(it.yearly)
            }
        }
    }

    private fun addGraph(s: List<DashboardResponse.Revenues.Item>?) {
        val dataList: ArrayList<BarData> = ArrayList()
        s?.forEach {
            val data = BarData(getGraphName(it.name.toString()), 10f, "0.0")
            dataList.add(data)
        }
        binding.graph.setDataList(dataList)
        binding.graph.build()
    }

    private fun getGraphName(name: String): String {
        /*2022-07-04*/
        var title = "Error"
        try {
            val cal = Calendar.getInstance()
            val s = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(name)
            s?.let {
                if (currentType == daily) {
                    title = SimpleDateFormat("EEE", Locale.US).format(s)
                } else if (currentType == weekly) {
                    title = SimpleDateFormat("dd MMM", Locale.US).format(s)
                } else if (currentType == monthly) {
                    title = SimpleDateFormat("MMM", Locale.US).format(s)
                } else if (currentType == yearly) {
                    title = SimpleDateFormat("yyyy", Locale.US).format(s)
                }
            }
        } catch (e: Exception) {

        }
        return title

    }

    private fun processLinks(data: PlanResponse?) {
        data?.let {
            showSheetIntro(it.addProductFormLink, it.subscribeLink)
        }

    }

    private fun updateView(data: DashboardResponse?) {
        adapter.clearList()
        data?.let { it ->
            it.pending?.let {
                adapter.addData(
                    DashBean(
                        R.drawable.ic_dash_order,
                        "Pending",
                        "Orders -",
                        "${it.count}",
                        UtilMethods.getCurrencyFormat(context, it.amount?.toDouble(), false),
                        R.id.frg_order
                    )
                )
            }
            it.outstanding?.let {
                adapter.addData(
                    DashBean(
                        R.drawable.ic_dash_outst,
                        "Amount",
                        "Outst. -",
                        "${it.count}",
                        UtilMethods.getCurrencyFormat(context, it.amount?.toDouble(), false)
                    )
                )
            }
            it.coinsBalance?.let {
                adapter.addData(
                    DashBean(
                        R.drawable.ic_dash_tick,
                        "Riggle",
                        "Coin Settl.",
                        null,
                        it.toString()
                    )
                )
            }
            it.credit?.let {
                adapter.addData(
                    DashBean(
                        R.drawable.ic_dash_bag,
                        "Retailer",
                        "Credit. -",
                        "${it.count}",
                        UtilMethods.getCurrencyFormat(context, it.amount?.toDouble(), false)
                    )
                )
            }

            /*  changeValueProgress(
                  binding.vOne,
                  it.pending_count.toFloat(),
                  100f,
                  "Pending Order",
                  UtilMethods.getCurrencyFormat(context, it.pending_amount)
              )
              changeValueProgress(
                  binding.vTwo,
                  it.outstanding_count.toFloat(),
                  100f,
                  "Amt Outstand",
                  UtilMethods.getCurrencyFormat(context, it.outstanding_amount)
              )
              changeValueProgress(
                  binding.v3,
                  it.riggle_credit_count.toFloat(),
                  100f,
                  "Rig Coin Settlement",
                  UtilMethods.getCurrencyFormat(context, it.riggle_credit_amount)
              )
              changeValueProgress(
                  binding.v4,
                  it.delivered_count.toFloat(),
                  100f,
                  "Statement",
                  UtilMethods.getCurrencyFormat(context, it.total_amount)
              )*/
        }
    }

    private fun changeValueProgress(
        vOne: ViewCircleGraphBinding,
        progress: Float,
        max: Float,
        title: String,
        amount: String
    ) {
        vOne.tvTitle.text = title
        vOne.tvAmount.text = amount
    }


    override fun onStart() {
        super.onStart()
        viewmodel.dashboard()
        viewmodel.getCompany()
    }

    private fun updateUserInfo(company: Company?) {
        company?.extra?.let {
            if (it.isPlanActive == true) {
                binding.btnActivate.visibility = View.GONE;
            } else {
                binding.btnActivate.visibility = View.VISIBLE;
            }
        }
    }

    private fun initViews() {
        sharedPrefManager.getCurrentUser()?.company?.let {
            binding.toolbar.tvTitle.text = it.name
        }
        adapter =
            RVAdapter(R.layout.view_circle_graph, BR.bean, object : RVAdapter.Callback<DashBean> {
                override fun onItemClick(v: View, m: DashBean) {
                    if (m.id != 0) {
                        Navigation.findNavController(v).navigate(m.id)
                    }
                }
            })
        binding.rvOne.layoutManager = GridLayoutManager(context, 2)
        binding.rvOne.adapter = adapter

    }

    private fun showSheetIntro(linkAddProduct: String?, linkSubscribe: String?) {
        sheet1 = BaseCloseSheet(
            R.layout.sheet_intro,
            object : BaseCloseSheet.ClickListener<SheetIntroBinding> {
                override fun onClick(view: View) {
                    when (view.id) {
                        R.id.ll_add_product ->
                            openBrowser(linkAddProduct)
                        R.id.ll_subscribe ->
                            openBrowser(linkSubscribe)
                        R.id.btn_login -> {
                            this@DashBoardFragment.view?.let {
                                sheet1?.dismissAllowingStateLoss()
                                Navigation.findNavController(it).navigate(R.id.frag_subscription)
                            }

                        }
                    }
                }

                override fun onViewCreated(b: SheetIntroBinding) {
                }

                override fun onClose() {
                    binding.blur.visibility = View.GONE
                }
            })
        showSheetBlur(binding.main, binding.blur, sheet1)
    }

    private fun openBrowser(link: String?) {
        sheet1?.dismissAllowingStateLoss()
        try {
            val url = Uri.parse(link)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = url
            startActivity(i)
        } catch (e: Exception) {
            showErrorToast("Url not valid")
        }
    }


    override fun onDestroy() {
        sheet1?.isVisible?.let {
            sheet1?.dismissAllowingStateLoss()
        }
        super.onDestroy()
    }

    data class DashBean(
        val icon: Int,
        val title: String,
        val title2: String,
        val ordertext: String?,
        val amount: String,
        val id: Int = 0,
    )
}