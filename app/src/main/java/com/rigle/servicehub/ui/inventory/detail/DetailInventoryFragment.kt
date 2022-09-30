package com.rigle.servicehub.ui.inventory.detail

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.CartResponse
import com.rigle.servicehub.data.model.Stock
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.FragmentDetailInventoryBinding
import com.rigle.servicehub.databinding.HolderInventoryProductsBinding
import com.rigle.servicehub.ui.base.BaseFragment
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.RVAdapter
import com.rigle.servicehub.ui.base.location.LocationHandler
import com.rigle.servicehub.ui.base.location.LocationResultListener
import com.rigle.servicehub.ui.base.permission.PermissionHandler
import com.rigle.servicehub.ui.base.permission.Permissions
import com.rigle.servicehub.utils.Constants
import com.rigle.servicehub.utils.Constants.Companion.locationHandler
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.*
import com.rigle.servicehub.utils.view.MyCheckBox
import com.google.android.material.progressindicator.LinearProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class DetailInventoryFragment : BaseFragment<FragmentDetailInventoryBinding>() {
    private var cartResponse: CartResponse? = null
    private lateinit var adapter: RVAdapter<Stock, HolderInventoryProductsBinding>
    private val viewmodel: DetailInventoryViewModel by viewModels()
    override fun getLayoutResource(): Int {
        return R.layout.fragment_detail_inventory
    }

    override fun getViewModel(): BaseViewModel = viewmodel
    override fun getHorizontalBar(): LinearProgressIndicator? {
        return binding.toolbar.pbOne
    }

    override fun onCreatedView(view: View, savedInstanceState: Bundle?) {
        initViews()
        initAdapter()
        initLocation()
        viewmodel.onClick.observe(viewLifecycleOwner, Observer<View> { v ->
            when (v.id) {
                R.id.iv_back -> {
                    Navigation.findNavController(v).navigateUp()
                }
                R.id.btn_submit -> {
                    getEditQty()?.let { it ->
                        cartResponse?.id?.let { it1 ->
                            checkLocation()
                        } ?: {
                            showErrorToast("Getting cart detail")
                            getCart()
                        }

                    }
                }
            }
        })

        viewmodel.obrStock.observe(
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
                        updateEmptyView(adapter.itemCount, "No Stocks")
                    }
                    Status.ERROR -> {
                        binding.loader.hideLoading()
                        updateEmptyView(adapter.itemCount, resource.message)
                    }
                    Status.CACHED -> {

                    }
                }
            })
        viewmodel.obrCart.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        cartResponse = resource.data
                    }
                    Status.ERROR -> {
                        hideLoading()
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
                        showSuccessToast(resource.message)
                        arguments?.getString("id")?.let {
                            viewmodel.getStock(it)
                        }
                    }
                    Status.ERROR -> {
                        hideLoading()
                        arguments?.getString("id")?.let {
                            viewmodel.getStock(it)
                        }

                    }
                    Status.CACHED -> {

                    }
                }
            })
        viewmodel.obrUpdateQty.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        Constants.locationHandler?.lostLocation?.let {
                            viewmodel.submitOrder(JSONObject().apply {
                                put("location", "${it.latitude}:${it.longitude}")
                                put("cart_id", cartResponse?.id)
                            })
                        }

                    }
                    Status.ERROR -> {
                        hideLoading()
                        arguments?.getString("id")?.let {
                            viewmodel.getStock(it)
                        }

                    }
                    Status.CACHED -> {

                    }
                }
            })
        viewmodel.obrSubmit.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showSuccessToast("Order created successfully")
                        Navigation.findNavController(view).navigateUp()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        arguments?.getString("id")?.let {
                            viewmodel.getStock(it)
                        }

                    }
                    Status.CACHED -> {

                    }
                }
            })
    }

    private fun getCart() {
        arguments?.getString("id")?.let {
            viewmodel.getCart(hashMapOf<String, String>().apply {
                put("brand_id", it)
            }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        arguments?.getString("id")?.let {
            viewmodel.getStock(it)
        }
        getCart()
        updateSubmitButton()
    }

    private fun updateSubmitButton() {
        binding.btnenable = getEditQty() != null
    }

    private fun updateEmptyView(itemCount: Int, message: String?) {
        if (itemCount > 0) {
            binding.vEmpty.hideEmptyView()
        } else {
            binding.vEmpty.showEmptyView(message)
        }
    }

    private fun initAdapter() {
        adapter = object : RVAdapter<Stock, HolderInventoryProductsBinding>(
            R.layout.holder_inventory_products,
            BR.bean,
            object : RVAdapter.Callback<Stock> {
                override fun onItemClick(v: View, m: Stock) {

                }

                override fun onItemClick(v: View, m: Stock, position: Int) {
                    if (v.id == R.id.iv_minus) {
                        if (m.newQty > 0) {
                            updateQty(-1, m, position)
                        }
                    } else if (v.id == R.id.iv_plus) {
                        updateQty(1, m, position)
                    }
                }
            }


        ) {
            override fun onBind(
                binding: HolderInventoryProductsBinding,
                bean: Stock,
                position: Int
            ) {
                super.onBind(binding, bean, position)
                binding.cbOne.onCheckChangeListener = null
                bean.isActive?.let {
                    binding.cbOne.checked = it
                }
                binding.cbOne.onCheckChangeListener = object : MyCheckBox.OnCheckChangeListener {
                    override fun onCheckChanged(checked: Boolean) {
                        viewmodel.updateStockProduct(bean.id.toString(), checked)
                    }
                }
                if(position<adapter.itemCount-1){
                    binding.space.visibility=View.GONE
                }else
                    binding.space.visibility=View.VISIBLE

            }
        }
        binding.rvOne.layoutManager = LinearLayoutManager(context)
        binding.rvOne.adapter = adapter

    }

    private fun updateQty(i: Int, m: Stock, position: Int) {
        m.product?.distributorMoq?.let {
            val count = m.newQty + i * it
            if (count >= 0) {
                adapter.getData(position)?.newQty = count
                adapter.notifyItemChanged(position)
                updateSubmitButton()
            }
        }
    }

    private fun getEditQty(): JSONObject? {
        val s = JSONObject()
        val array = JSONArray()
        adapter.getDataList().forEach {
            if (it.newQty > 0) {
                val obj = JSONObject()
                obj.put("product", it.product?.id.toString())
                obj.put("quantity", it.newQty)
                array.put(obj)
            }
        }
        if (array.length() == 0) {
            return null
        }
        s.put("products", array)
        return s
    }


    private fun initViews() {
        binding.toolbar.tvTitle.text = getString(R.string.inventory)
    }


    private fun initLocation() {
        Constants.locationHandler =
            LocationHandler(requireActivity(), object : LocationResultListener {
                override fun onLocationRefresh(location: Location) {
                    getEditQty()?.let { it ->
                        cartResponse?.id?.let { it1 ->
                            viewmodel.updateStockQty(it1.toString(), it)
                        } ?: {
                            showErrorToast("Getting cart detail")
                            getCart()
                        }

                    }
                }

                override fun onOldLocation(location: Location) {

                }

                override fun onRefreshStart() {
                    showLoading("Getting Location")
                }

                override fun onRefreshStop() {
                    hideLoading()
                }

                override fun onError(error: String?) {
                    showErrorToast(error.toString())
                }
            })
    }

    private fun checkLocation() {
        Permissions.check(
            activity, arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION),
            0,null,
            object : PermissionHandler() {
                override fun onGranted() {
                    locationHandler?.getUserLocation()
                }

                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                    super.onDenied(context, deniedPermissions)
                    showInfoToast("Accept location permission to continue")
                }
            })
    }


}
