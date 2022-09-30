package com.rigle.servicehub.ui.coupon

import android.content.Context
import android.content.Intent
import android.text.InputFilter
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.Brand
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.ActivityAddCouponBinding
import com.rigle.servicehub.databinding.HolderBrandsBinding
import com.rigle.servicehub.databinding.SheetBrandsBinding
import com.rigle.servicehub.ui.base.BaseActivity
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.RVAdapter
import com.rigle.servicehub.ui.base.sheet.BaseCloseSheet
import com.rigle.servicehub.utils.Constants
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.showErrorToast
import com.rigle.servicehub.utils.extension.showInfoToast
import com.rigle.servicehub.utils.extension.showSheetBlur
import com.rigle.servicehub.utils.extension.showSuccessToast
import com.google.gson.JsonElement
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class AddCouponActivity : BaseActivity<ActivityAddCouponBinding>() {

    val viewModel: AddCouponActivityVM by viewModels()

    private var sheet1: BaseCloseSheet<SheetBrandsBinding>? = null

    companion object {
        fun newIntent(activity: Context): Intent {
            val intent = Intent(activity, AddCouponActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_add_coupon
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        binding.toolbar.tvTitle.text = "Add Coupon"
        initView()
        viewModel.onClick.observe(this) { it ->
            when (it?.id) {
                R.id.iv_back -> {
                    finish()
                }
                R.id.btn_activate -> {
                    addCoupon()
                }
                R.id.et_brand -> {
                    viewModel.obrBrands.value?.data?.results?.let {
                        showBrands(it)
                    }
                }
            }
        }
        viewModel.obrBrands.observe(
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
        viewModel.obrAddOffer.observe(
            this,
            SingleRequestEvent.RequestObserver<JsonElement> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showSuccessToast(resource.message)
                        sheet1?.dismissAllowingStateLoss()
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

    private fun initView() {
        binding.rbFlat.isChecked = true
        updateUi(R.id.rb_flat)
        binding.rgOne.setOnCheckedChangeListener { group, checkedId ->
            updateUi(checkedId)
        }
    }

    private fun updateUi(checkedId: Int) {
        when (checkedId) {
            R.id.rb_flat -> {
                binding.etOfferValue.hint = "Rs."
                binding.etMinorder.hint = "Rs."
                binding.etOfferValue.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(8))
            }
            R.id.rb_percent -> {
                binding.etOfferValue.hint = "%"
                binding.etOfferValue.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(2))
                binding.etMinorder.hint = "Rs."
            }
        }
        binding.etOfferValue.text = null
        binding.etMinorder.text = null
    }


    override fun onStart() {
        super.onStart()
        viewModel.getBrands()
    }

    private fun showBrands(list: List<Brand>) {
        sheet1 = BaseCloseSheet(
            R.layout.sheet_brands,
            object : BaseCloseSheet.ClickListener<SheetBrandsBinding> {
                override fun onClick(view: View) {

                }

                override fun onViewCreated(b: SheetBrandsBinding) {
                    val adapter = object : RVAdapter<Brand, HolderBrandsBinding>(
                        R.layout.holder_brands, BR.bean,
                        object : RVAdapter.Callback<Brand> {
                            override fun onItemClick(v: View, m: Brand) {
                                binding.brand = m
                                sheet1?.dismissAllowingStateLoss()
                            }
                        }) {
                        override fun onBind(
                            binding: HolderBrandsBinding,
                            bean: Brand,
                            position: Int
                        ) {
                            super.onBind(binding, bean, position)
                            binding.cbOne.visibility = View.GONE
                        }
                    }


                    adapter.setDataList(list)
                    b.rvOne.layoutManager = LinearLayoutManager(this@AddCouponActivity)
                    b.rvOne.adapter = adapter
                }

                override fun onClose() {
                    binding.blur.visibility = View.GONE
                }
            }, false
        )
        showSheetBlur(binding.mainView, binding.blur, sheet1)
    }

    private fun addCoupon() {
        val jsonObject = JSONObject()
        if (binding.brand == null) {
            showInfoToast("Select brand")
            viewModel.getBrands()
            return
        }
        binding.brand?.let {
            jsonObject.put("brand", it.id)
        }
        binding.etOfferValue.text.toString().let { it1 ->
            if (it1.isEmpty()) {
                binding.etOfferValue.error = "Invalid"
                return
            }
            jsonObject.put("discount_amount", it1)
        }
        binding.etMinorder.text.toString().let { it1 ->
            if (it1.isEmpty()) {
                binding.etMinorder.error = "Invalid"
                return
            }
            jsonObject.put("min_amount", it1)
        }
        binding.etNumber.text.toString().let { it1 ->
            if (it1.isEmpty()) {
                binding.etNumber.error = "Invalid"
                return
            }
            jsonObject.put("ordered_quantity", it1)
        }

        if (binding.rgOne.checkedRadioButtonId == R.id.rb_percent)
            jsonObject.put("discount_type", Constants.OFFER_PERCENT)
        else
            jsonObject.put("discount_type", Constants.OFFER_FIXED)
        if (binding.rgTwo.checkedRadioButtonId == R.id.rb_condition_1)
            jsonObject.put("usage_type", Constants.OFFER_ONCE)
        else
            jsonObject.put("usage_type", Constants.OFFER_ONCE_A_DAY)

        viewModel.addOffer(jsonObject)
    }
}