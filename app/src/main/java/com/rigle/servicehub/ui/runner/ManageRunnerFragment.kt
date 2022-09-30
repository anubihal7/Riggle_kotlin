package com.rigle.servicehub.ui.runner

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.RunnerResponse
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentManageRunnerBinding
import com.rigle.servicehub.databinding.HolderRunnerBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.RVAdapter
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.hideEmptyView
import com.rigle.servicehub.utils.extension.hideLoading
import com.rigle.servicehub.utils.extension.showEmptyView
import com.rigle.servicehub.utils.extension.showLoading
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ManageRunnerFragment : BaseFragment<FragmentManageRunnerBinding>() {
    private lateinit var adapter: RVAdapter<RunnerResponse, HolderRunnerBinding>
    private val viewmodel: MangeRunnerViewModel by viewModels()
    private lateinit var calendar: Calendar
    override fun getLayoutResource(): Int {
        return R.layout.fragment_manage_runner
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
                R.id.ll_date -> {
                    showDatePicker()
                }
                R.id.btn_go,
                R.id.btn_add -> {
                    Navigation.findNavController(v).navigate(
                        R.id.frg_edit_account, bundleOf(
                            Pair("title", getString(R.string.add_runner)),
                            Pair("type", 3)
                        )
                    )
                }
            }
        })

        viewmodel.obrRunner.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        binding.loader.showLoading()
                    }
                    Status.SUCCESS -> {
                        binding.loader.hideLoading()
                        adapter.setDataList(resource.data)
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
        viewmodel.obrUpdate.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        viewmodel.getRunner(calendar.time)
                        updateTimeView()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        updateEmptyView(adapter.itemCount, resource.message)
                    }
                    Status.CACHED -> {

                    }
                }
            })
    }

    override fun onStart() {
        super.onStart()
        viewmodel.getRunner(calendar.time)
        updateTimeView()
    }

    private fun updateTimeView() {
        binding.tvDate.text = SimpleDateFormat("dd MMM yyyy", Locale.US).format(calendar.time)
    }

    private fun updateEmptyView(itemCount: Int, message: String?) {
        if (itemCount > 0) {
            if (itemCount > 1)
                binding.tvCount.text = "You have $itemCount runners"
            else
                binding.tvCount.text = "You have $itemCount runner"
            binding.tvCount.visibility = View.VISIBLE
            binding.empty.hideEmptyView()
            binding.btnAdd.visibility = View.VISIBLE
        } else {
            binding.empty.showEmptyView(message)
            binding.btnAdd.visibility = View.GONE
            binding.tvCount.visibility = View.INVISIBLE
        }
    }

    private fun initAdapter() {
        adapter =
            RVAdapter(R.layout.holder_runner, BR.bean, object : RVAdapter.Callback<RunnerResponse> {
                override fun onItemClick(v: View, m: RunnerResponse) {
                    if (v.id == R.id.tv_detail) {
                        val bundle = Bundle()
                        bundle.putString("id", m.runner?.id.toString())
                        bundle.putSerializable("date", calendar.time)
                        Navigation.findNavController(v).navigate(R.id.frg_detail_runner, bundle)
                    } else if (v.id == R.id.tv_edit) {
                        val bundle = Bundle()
                        bundle.putParcelable("runner", m.runner)
                        Navigation.findNavController(v).navigate(R.id.frg_update_runner, bundle)

                    } else if (v.id == R.id.tv_disable) {
                        m.runner?.isActive?.let {
                            viewmodel.updateRunner(m.runner.id.toString(), !it)
                        }

                    }
                }
            })
        binding.rvOne.layoutManager = LinearLayoutManager(context)
        binding.rvOne.adapter = adapter

    }

    private fun showDatePicker() {
        val s = MaterialDatePicker
            .Builder
            .datePicker()
            .setTitleText("Select date")
            .setCalendarConstraints(
                CalendarConstraints.Builder().setOpenAt(calendar.timeInMillis).build()
            )
            .build()
        s.addOnPositiveButtonClickListener {
            calendar.timeInMillis = it
            viewmodel.getRunner(calendar.time)
            updateTimeView()
        }
        s.show(childFragmentManager, "DATE_PICKER")
    }


    private fun initViews() {
        calendar = Calendar.getInstance()
        binding.empty.btnGo.text = getString(R.string.add_runner)
        binding.toolbar.tvTitle.text = getString(R.string.manage_runner)
    }
}
