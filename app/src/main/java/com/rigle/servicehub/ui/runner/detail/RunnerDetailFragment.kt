package com.rigle.servicehub.ui.runner.detail

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.RunnerResponse
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentRunnerDetailBinding
import com.rigle.servicehub.databinding.ViewSummeryBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.hideLoading
import com.rigle.servicehub.utils.extension.showLoading
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RunnerDetailFragment : BaseFragment<FragmentRunnerDetailBinding>() {
    private var runnerId: String? = null
    private val viewmodel: RunnerDetailViewModel by viewModels()
    private lateinit var calendar: Calendar
    override fun getLayoutResource(): Int {
        return R.layout.fragment_runner_detail
    }

    override fun getViewModel(): BaseViewModel = viewmodel


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
            callApi()
            updateTimeView()
        }
        s.show(childFragmentManager, "DATE_PICKER")
    }

    private fun callApi() {
        viewmodel.getRunnerDetail(calendar.time, runnerId.toString())
    }


    private fun updateTimeView() {
        binding.tvDate.text = SimpleDateFormat("dd MMM yyyy", Locale.US).format(calendar.time)
    }

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            if (v.id == R.id.iv_back) {
                Navigation.findNavController(v).navigateUp()
            } else if (v.id == R.id.ll_date) {
                showDatePicker()
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
                        setDataInUi(resource.data)
                    }
                    Status.ERROR -> {
                        binding.loader.hideLoading()
                    }
                    Status.CACHED -> {

                    }
                }
            })

    }

    override fun onStart() {
        super.onStart()
        callApi()
    }

    private fun setDataInUi(data: RunnerResponse?) {
        data?.let {
            binding.tvName.text = it.runner?.fullName
            binding.vMain.removeAllViews()
            DataBindingUtil.inflate<ViewSummeryBinding>(
                layoutInflater,
                R.layout.view_summery,
                binding.vMain,
                false
            ).apply {
                line.visibility = View.VISIBLE
                binding.vMain.addView(root)
            }

            DataBindingUtil.inflate<ViewSummeryBinding>(
                layoutInflater,
                R.layout.view_summery,
                binding.vMain,
                false
            ).apply {
                tv1.text = "Cash"
                tv2.text = it.cashTrips.toString()
                tv3.text = it.cashTotal.toString()
                setBlackColor(this)
                binding.vMain.addView(root)
            }


            DataBindingUtil.inflate<ViewSummeryBinding>(
                layoutInflater,
                R.layout.view_summery,
                binding.vMain,
                false
            ).apply {
                tv1.text = "Cheque"
                tv2.text = it.chequeTrips.toString()
                tv3.text = it.chequeTotal.toString()
                setBlackColor(this)
                binding.vMain.addView(root)
            }

            DataBindingUtil.inflate<ViewSummeryBinding>(
                layoutInflater,
                R.layout.view_summery,
                binding.vMain,
                false
            ).apply {
                tv1.text = "UPI"
                tv2.text = it.upiTrips.toString()
                tv3.text = it.upiTotal.toString()
                setBlackColor(this)
                binding.vMain.addView(root)
            }

            DataBindingUtil.inflate<ViewSummeryBinding>(
                layoutInflater,
                R.layout.view_summery,
                binding.vMain,
                false
            ).apply {
                tv1.text = "Riggle Credit"
                tv2.text = it.riggleCreditTrips.toString()
                tv3.text = it.riggleCreditTotal.toString()
                setBlackColor(this)
                binding.vMain.addView(root)
            }


            DataBindingUtil.inflate<ViewSummeryBinding>(
                layoutInflater,
                R.layout.view_summery,
                binding.vMain,
                false
            ).apply {
                tv1.text = "Riggle Coins"
                tv2.text = it.redeemedCoins.toString()
                tv3.text = it.redeemedCoinsValue.toString()
                setBlackColor(this)
                binding.vMain.addView(root)
            }

            DataBindingUtil.inflate<ViewSummeryBinding>(
                layoutInflater,
                R.layout.view_summery,
                binding.vMain,
                false
            ).apply {
                tv1.text = "Amount Outstanding"
                tv2.text = it.pendingTrips.toString()
                tv3.text = it.pendingTotal.toString()
                setBlackColor(this)
                binding.vMain.addView(root)
            }



            DataBindingUtil.inflate<ViewSummeryBinding>(
                layoutInflater,
                R.layout.view_summery,
                binding.vMain,
                false
            ).apply {
                tv1.text = "Total"
                tv1.typeface = ResourcesCompat.getFont(requireContext(), R.font.rubik_medium)
                tv2.text = it.trips.toString()
                tv2.typeface = ResourcesCompat.getFont(requireContext(), R.font.rubik_medium)
                tv3.text = it.totalSettlement.toString()
                tv3.typeface = ResourcesCompat.getFont(requireContext(), R.font.rubik_medium)
                back.visibility = View.VISIBLE
                binding.vMain.addView(root)
            }
        }
    }


    private fun initViews() {
        calendar = Calendar.getInstance()
        binding.toolbar.tvTitle.text = getString(R.string.settlements)
        val date = arguments?.getSerializable("date") as Date
        runnerId = arguments?.getString("id", "")
        calendar.time = date
        updateTimeView()
    }

    private fun setBlackColor(viewSummeryBinding: ViewSummeryBinding?) {
        viewSummeryBinding?.let {
            it.tv1.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            it.tv2.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            it.tv3.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
    }

}