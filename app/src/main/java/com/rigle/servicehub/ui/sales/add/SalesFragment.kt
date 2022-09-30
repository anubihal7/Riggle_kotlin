package com.rigle.servicehub.ui.sales.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.Brand
import com.rigle.servicehub.data.model.SalesPerson
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentSalesBinding
import com.rigle.servicehub.databinding.HolderBrandsBinding
import com.rigle.servicehub.databinding.SheetBrandsBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.RVAdapter
import com.rigle.servicehub.ui.base.sheet.BaseCloseSheet
import com.rigle.servicehub.utils.callbacks.Validator
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.JsonElement
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SalesFragment : BaseFragment<FragmentSalesBinding>() {
    private val viewmodel: SalesViewModel by viewModels()
    private lateinit var adapter: RVAdapter<Brand, HolderBrandsBinding>
    private var id: String? = null
    private var sheet1: BaseCloseSheet<SheetBrandsBinding>? = null
    private lateinit var calendar: Calendar
    private val brandsList = mutableListOf<Brand>()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_sales
    }

    override fun getViewModel(): BaseViewModel = viewmodel


    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        addAdapter()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            if (v.id == R.id.iv_back) {
                Navigation.findNavController(v).navigateUp()
            } else if (v.id == R.id.btn_add) {
                addUser()
            } else if (v.id == R.id.et_dob) {
                showDatePicker()
            } else if (v.id == R.id.et_brand) {
                showBrands()
            }
        })
        viewmodel.obrUpdate.observe(
            this,
            SingleRequestEvent.RequestObserver<JsonElement> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showSuccessToast(resource.message)
                        Navigation.findNavController(view).navigateUp()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {

                    }
                }
            })
        viewmodel.obrSalesPerson.observe(
            this,
            SingleRequestEvent.RequestObserver<SalesPerson> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        binding.loader.showLoading()
                    }
                    Status.SUCCESS -> {
                        binding.loader.hideLoading()
                        addDataInUI(resource.data)
                    }
                    Status.ERROR -> {
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {
                    }
                }
            })
        viewmodel.obrBrands.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        adapter.setDataList(resource.data?.results)
                    }
                    Status.ERROR -> {
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {

                    }
                }
            })
    }

    private fun addAdapter() {
        adapter = object : RVAdapter<Brand, HolderBrandsBinding>(
            R.layout.holder_brands, BR.bean,
            object : RVAdapter.Callback<Brand> {
                override fun onItemClick(v: View, m: Brand) {

                }
            }) {
            override fun onBind(
                binding: HolderBrandsBinding,
                bean: Brand,
                position: Int
            ) {
                super.onBind(binding, bean, position)
                binding.cbOne.setOnCheckedChangeListener(null)
               binding.cbOne.isChecked=bean.checked
                binding.cbOne.setOnCheckedChangeListener { buttonView, isChecked ->
                    adapter.getDataList()[position].checked = isChecked
                }
            }
        }
    }

    private fun addDataInUI(data: SalesPerson?) {
        data?.let {
            viewmodel.fieldName.set(it.fullName)
            viewmodel.fieldPhone.set(it.mobile)
        }
    }

    private fun addUser() {
        when {
            viewmodel.fieldName.get().isNullOrBlank() -> {
                binding.etname.error = "Invalid"
            }
            viewmodel.fieldPhone.get().isNullOrBlank() -> {
                binding.etPhone.error = "Invalid"
            }
            binding.etBrand.text.toString().isBlank() -> {
                binding.etBrand.error = "Select Brand"
                showBrands()
            }
            binding.dob.isNullOrBlank() -> {
                binding.etDob.error = "Invalid"
            }
            else -> {
                val name = viewmodel.fieldName.get()?.split(" ")
                val parm = JSONObject()
                name?.let {
                    if (it.isNotEmpty())
                        parm.put("first_name", it[0])
                    if (it.size > 1)
                        parm.put("last_name", it[1])
                    else
                        parm.put("last_name", "")
                }

                val stringBuilder = StringBuilder()
                brandsList.forEachIndexed { index, brand ->
                    if (index != 0)
                        stringBuilder.append(",")
                    stringBuilder.append(brand.id)
                }
                parm.put("mapped_brands", stringBuilder.toString())

                parm.put("mobile", viewmodel.fieldPhone.get().toString())
                parm.put("role", "sales_man")
                parm.put("designation", "Sales Executive")
                parm.put("dob", SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time))
                parm.put("company", sharedPrefManager.getCurrentUser()?.company?.id)
                id?.let {
                    viewmodel.updateSalesPerson(it, parm)
                } ?: run {
                    viewmodel.addSalesPerson(parm)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (adapter.itemCount == 0)
            viewmodel.getBrands()
        id?.let {
            viewmodel.getSalesPerson(it)
        }
    }

    private fun initViews() {
        id = arguments?.getString("id")
        binding.toolbar.tvTitle.text = getString(R.string.sales_person)
        binding.etname.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
                return s.length > 3
            }
        })
        binding.etPhone.attachValidator(object : Validator {
            override fun isValidField(s: String): Boolean {
                return s.length >= 10
            }
        })
        calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -20)
    }

    private fun showBrands() {
            sheet1 = BaseCloseSheet(
                R.layout.sheet_brands,
                object : BaseCloseSheet.ClickListener<SheetBrandsBinding> {
                    override fun onClick(view: View) {
                        if (view.id == R.id.tick) {
                            brandsList.clear()
                            adapter.getDataList().forEach {
                                if (it.checked)
                                    brandsList.add(it)
                            }
                            updateBrandUI()
                            sheet1?.dismissAllowingStateLoss()
                        }
                    }

                    override fun onViewCreated(b: SheetBrandsBinding) {
                        b.rvOne.layoutManager = LinearLayoutManager(context)
                        b.rvOne.adapter = adapter
                    }

                    override fun onClose() {
                        binding.blur.visibility = View.GONE
                    }
                }, false
            )
        showSheetBlur(binding.main, binding.blur, sheet1)
    }

    private fun updateBrandUI() {
        val stringBuilder = StringBuilder()
        brandsList.forEachIndexed { index, brand ->
            if (index != 0)
                stringBuilder.append(",")
            stringBuilder.append(brand.name)
        }
        binding.etBrand.setText(stringBuilder.toString())
    }

    private fun showDatePicker() {
        val s = MaterialDatePicker
            .Builder
            .datePicker()
            .setTitleText("Date of birth")
            .setCalendarConstraints(
                CalendarConstraints.Builder().setOpenAt(calendar.timeInMillis)
                    .setEnd(Calendar.getInstance().timeInMillis).build()
            )
            .build()
        s.addOnPositiveButtonClickListener {
            calendar.timeInMillis = it
            binding.dob = SimpleDateFormat(
                "dd MMMM yyyy",
                Locale.US
            ).format(calendar.time)
        }
        s.show(childFragmentManager, "DATE_PICKER")
    }

}
