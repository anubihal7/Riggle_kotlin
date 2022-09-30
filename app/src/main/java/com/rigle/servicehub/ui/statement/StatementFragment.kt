package com.rigle.servicehub.ui.statement

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ShareCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentStatementBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.permission.PermissionHandler
import com.rigle.servicehub.ui.base.permission.Permissions
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.showErrorToast
import com.rigle.servicehub.utils.extension.showSuccessToast
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.JsonElement
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class StatementFragment : BaseFragment<FragmentStatementBinding>() {
    private var type: Int = 0
    private val viewmodel: StatementViewModel by viewModels()
    private lateinit var calendarStart: Calendar
    private lateinit var calendarEnd: Calendar

    override fun getLayoutResource(): Int {
        return R.layout.fragment_statement
    }

    override fun getViewModel(): BaseViewModel = viewmodel

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
      // downloadPdf("https://assets.riggleapp.in/order/90/29ce63e1-32f5-4de3-be70-fd36806018d9_Undertaking_RSP-1220-40-RRT-87-90dd6fc02e-d966-4f7.pdf")
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            when (v.id) {
                R.id.iv_back -> {
                    Navigation.findNavController(v).navigateUp()
                }
                R.id.btn_download -> {
                    type = 2
                    getAPiData()
                }
                R.id.tv_share -> {
                    type = 1
                    getAPiData()
                }
            }
        })
        viewmodel.obrData.observe(
            this,
            SingleRequestEvent.RequestObserver<JsonElement> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        when (type) {
                            2 ->
                                downloadPdf(resource.data?.asJsonObject?.get("report_link")?.asString)
                            1 -> {
                                ShareCompat.IntentBuilder(requireContext())
                                    .setType("text/plain")
                                    .setChooserTitle("Share statement")
                                    .setText(resource.data?.asJsonObject?.get("report_link")?.asString)
                                    .startChooser();
                            }
                        }
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {
                    }
                }
            })
        binding.rbThisWeek.isChecked = true
    }


    private fun downloadPdf(invoiceFile: String?) {
        Permissions.check(requireContext(), arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), 0, null, object : PermissionHandler() {
            override fun onGranted() {

                try {
                    invoiceFile?.let {
                        val name = "Statement" + SimpleDateFormat(
                            "dd-mm-yyyyHH:mm:ss",
                            Locale.US
                        ).format(Calendar.getInstance().time) + ".pdf"
                        val downloadManager: DownloadManager? =
                            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
                        val request: DownloadManager.Request =
                            DownloadManager.Request(Uri.parse(it))
                        request.setTitle(name)
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        downloadManager?.enqueue(request)
                        showSuccessToast("Downloading")
                    }
                } catch (e: Exception) {
                    showErrorToast(e.message.toString())
                }
            }
        })
    }

    private fun initViews() {
        calendarStart = Calendar.getInstance()
        calendarEnd = Calendar.getInstance()
        binding.toolbar.tvTitle.text = getString(R.string.statements)
        binding.rgOne.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_custom) {
                showDateRangePicker()
            } else if (checkedId == R.id.rb_this_week) {
                setThisWeek()
            } else if (checkedId == R.id.rb_last_week) {
                setLastWeek()
            } else if (checkedId == R.id.rb_this_month) {
                setThisMonth()
            } else if (checkedId == R.id.rb_last_month) {
                setLastMonth()
            }
        }
    }

    private fun setThisWeek() {
        calendarStart = Calendar.getInstance()
        calendarEnd = Calendar.getInstance()
        val current = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        calendarStart.add(Calendar.DATE, -(current - 1))
        printDate()
    }

    private fun setLastWeek() {
        calendarStart = Calendar.getInstance()
        calendarEnd = Calendar.getInstance()
        val current = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        calendarStart.add(Calendar.DATE, -(current + 6))
        calendarEnd.add(Calendar.DATE, -current)
        printDate()
    }

    private fun setThisMonth() {
        calendarStart = Calendar.getInstance()
        calendarEnd = Calendar.getInstance()
        calendarStart.set(Calendar.DATE, 1)
        printDate()
    }

    private fun setLastMonth() {
        calendarStart = Calendar.getInstance()
        calendarEnd = Calendar.getInstance()
        calendarStart.set(Calendar.DATE, 1)
        calendarStart.add(Calendar.MONTH, -1)
        calendarEnd.set(Calendar.DATE, 1)
        calendarEnd.add(Calendar.DATE, -1)
        printDate()
    }

    private fun printDate() {
        binding.date.text = SimpleDateFormat(
            "dd MMMM yyyy",
            Locale.US
        ).format(calendarStart.time) + " to " + SimpleDateFormat(
            "dd MMMM yyyy", Locale.US
        ).format(calendarEnd.time)

    }

    private fun showDateRangePicker() {
        val s = MaterialDatePicker
            .Builder
            .dateRangePicker()
            .setTitleText("Select range")
            .build()
        s.addOnPositiveButtonClickListener {
            calendarStart = Calendar.getInstance()
            calendarEnd = Calendar.getInstance()
            calendarStart.timeInMillis = it.first
            calendarEnd.timeInMillis = it.second
            printDate()
        }
        s.addOnCancelListener {
            binding.rbThisWeek.isChecked = true
        }
        s.show(childFragmentManager, "DATE_PICKER_RANGE")
    }

    private fun getAPiData() {
        val map = hashMapOf<String, String>()
        map["start_date"] = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendarStart.time)
        map["end_date"] = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendarEnd.time)
        viewmodel.getSettlement(map)
    }

}