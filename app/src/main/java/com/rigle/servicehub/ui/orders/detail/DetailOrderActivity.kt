package com.rigle.servicehub.ui.orders.detail

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.view.View
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rigle.servicehub.BR
import com.rigle.servicehub.R
import com.rigle.servicehub.data.model.*
import com.rigle.servicehub.data.model.helper.Status
import com.rigle.servicehub.databinding.BsCreateMixBinding
import com.rigle.servicehub.databinding.FragmentDetailOrderBinding
import com.rigle.servicehub.databinding.SheetCancelOrderBinding
import com.rigle.servicehub.databinding.UpdateCreateMixBinding
import com.rigle.servicehub.ui.base.BaseActivity
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.ui.base.adapter.MultiAdapter
import com.rigle.servicehub.ui.base.adapter.RVAdapter
import com.rigle.servicehub.ui.base.permission.PermissionHandler
import com.rigle.servicehub.ui.base.permission.Permissions
import com.rigle.servicehub.ui.base.sheet.BaseCloseSheet
import com.rigle.servicehub.ui.base.sheet.MessageSheetBuilder
import com.rigle.servicehub.utils.Constants
import com.rigle.servicehub.utils.callbacks.Validator
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonElement
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class DetailOrderActivity : BaseActivity<FragmentDetailOrderBinding>() {
    private val viewmodel: DetailOrderViewModel by viewModels()
    private lateinit var adapter: MultiAdapter<Product>
    private var sheet1: BaseCloseSheet<SheetCancelOrderBinding>? = null
    private var sheet2: BaseCloseSheet<BsCreateMixBinding>? = null
    override fun getLayoutResource(): Int {
        return R.layout.fragment_detail_order
    }

    override fun getViewModel(): BaseViewModel = viewmodel
    override fun onCreateView() {
        initViews()
        setAdapter()
        viewmodel.onClick.observe(this, Observer<View> { v ->
            if (v.id == R.id.iv_back) {
                finish()
            } else if (v.id == R.id.ll_cancel) {
                binding.order?.let {
                    cancelOrder(it)
                }
            } else if (v.id == R.id.ll_invoice) {
                binding.order?.let {
                    downloadPdf(it.invoiceFile)
                    /* Permissions.check(
                         requireContext(),
                         android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                         null,
                         object : PermissionHandler() {
                             override fun onGranted() {
                                 downloadInvoice(it.invoiceFile)
                             }
                         })*/
                    // openBrowser(it.invoiceFile)
                }
            }
        })
        viewmodel.obrData.observe(
            this,
            SingleRequestEvent.RequestObserver { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        if (binding.order == null)
                            binding.loader.showLoading()
                    }
                    Status.SUCCESS -> {
                        resource.data?.let {
                            binding.order = it
                            if (it.status != Constants.STATUS_COMPLETED) {
                                updateAdapter(it.products)
                                updateEmptyView()
                            } else {
                                addWebView(it.invoiceFile)
                            }
                            updateRunner(it.assignedRunner)
                        }
                        updateViewVisibility()
                        binding.loader.hideLoading()
                    }
                    Status.ERROR -> {
                        binding.loader.hideLoading()
                    }
                    Status.CACHED -> {

                    }
                }
            })
        viewmodel.obrCancel.observe(
            this,
            SingleRequestEvent.RequestObserver<JsonElement> { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        showLoading(R.string.plz_wait)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showSuccessToast(resource.message)
                        finish()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        showErrorToast(resource.message)
                    }
                    Status.CACHED -> {

                    }
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
    }


    private fun addWebView(invoiceFile: String?) {
        if (invoiceFile.isNullOrEmpty()) {
            binding.tvError.visibility = View.VISIBLE
        }
        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$invoiceFile")
    }

    private fun updateRunner(assignedRunner: Runner?) {
        assignedRunner?.let {
            binding.tvRunner.text = "Runner:${it.fullName}"
        }
    }

    private fun setAdapter() {
        adapter = object : MultiAdapter<Product>(object : Callback<Product> {
            override fun onItemClick(v: View, m: Product) {
                if (v.id == R.id.iv_minus) {
                    if (m.products != null) {
                        showComboSheet(m)
                    } else {
                        updateOrder(m, -1)
                    }
                } else if (v.id == R.id.iv_plus) {
                    if (m.products != null) {
                        showComboSheet(m)
                    } else {
                        updateOrder(m, 1)
                    }
                } else if (v.id == R.id.tv_mix) {
                    if (m.products != null) {
                        showComboSheet(m)
                    }
                }
            }
        }) {
            override fun getLayoutResource(viewType: Int): Int {
                return when (viewType) {
                    1 -> R.layout.holder_product_enabled
                    2 -> R.layout.holder_product_disabled
                    else -> 0
                }
            }

            override fun getViewType(bean: Product): Int {
                return when (binding.order!!.status) {
                    Constants.STATUS_PENDING,
                    Constants.STATUS_CONFIRMED -> 1
                    else -> 2
                }
            }
        }
        binding.rvOne.layoutManager = LinearLayoutManager(this)
        binding.rvOne.adapter = adapter
    }

    private fun updateOrder(m: Product, ic: Int) {
        /*{
    "products": [{
        "product": 1,
        "quantity": 10
    }]
}*/
        var count = m.quantity!!
        m.product?.retailerMoq?.let {
            count += (it * ic)
        }
        if (count > 0) {
            val prodData = ArrayList<ProductEditData>()
            m.product?.id?.let { id ->
                prodData.add(ProductEditData(id, count, null))
            }
            binding.order?.id?.let {
                viewmodel.editProductQty(
                    it,
                    EditProductRequest(prodData)
                )
            }

        } else {
            val sheet = MessageSheetBuilder()
                .setTitleText("Remove")
                .setMessageText("Are you sure remove product?")
                .setPositiveButton("Remove")
                .setNegativeButton("cancel")
                .setListener(object : MessageSheetBuilder.Listener {
                    override fun onPositiveButtonClick(sheet: BottomSheetDialogFragment) {
                        sheet.dismissAllowingStateLoss()
                        val prodData = ArrayList<ProductEditData>()
                        m.product?.id?.let { id ->
                            prodData.add(ProductEditData(id, 0, null))
                        }
                        binding.order?.id?.let {
                            viewmodel.editProductQty(
                                it,
                                EditProductRequest(prodData)
                            )
                        }
                    }

                }).build()
            showSheet(sheet)
        }
    }

    private fun updateAdapter(products: List<Product>?) {
        adapter.setDataList(products)
    }

    private fun updateEmptyView() {
        if (adapter.itemCount > 0) {
            binding.vEmpty.hideEmptyView()
        } else {
            binding.vEmpty.showEmptyView("No Products")
        }
        binding.order?.let {
            when (it.status) {
                Constants.STATUS_PENDING -> {
                    if (adapter.itemCount > 0) {
                        binding.llCancel.visibility = View.VISIBLE
                    } else {
                        binding.llCancel.visibility = View.GONE
                    }

                }
                else ->
                    binding.llCancel.visibility = View.GONE
            }
        }
    }

    private fun updateViewVisibility() {
        binding.order?.let {
            when (it.status) {
                Constants.STATUS_COMPLETED -> {
                    binding.vWebview.visibility = View.VISIBLE
                    binding.vRv.visibility = View.GONE
                }
                else -> {
                    binding.vWebview.visibility = View.GONE
                    binding.vRv.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun initViews() {
        binding.toolbar.tvTitle.text = getString(R.string.orders)

    }

    private fun getData() {
        intent?.getStringExtra("orderId")?.let {
            val map = hashMapOf<String, String>()
            map["expand"] = "buyer.admin,products.product,assigned_runner"
            map["build_edit_view"] = "true"
            viewmodel.getData(it, map)
        }
    }

    private fun cancelOrder(orderBean: OrderBean) {
        var valid = false;
        sheet1 = BaseCloseSheet(
            R.layout.sheet_cancel_order,
            object : BaseCloseSheet.ClickListener<SheetCancelOrderBinding> {
                override fun onClick(view: View) {
                    if (view.id == R.id.btn_submit) {
                        if (valid) {
                            sheet1?.getBinding()?.let {
                                val parm = JSONObject()
                                parm.put("remark", it.etRemark.text.toString())
                                viewmodel.cancelOrder(orderBean.id.toString(), parm)
                                sheet1?.dismissAllowingStateLoss()
                            }
                        } else {
                            showInfoToast("Remark not valid")
                        }
                    }
                }

                override fun onViewCreated(b: SheetCancelOrderBinding) {
                    b.etRemark.attachValidator(object : Validator {
                        override fun isValidField(s: String): Boolean {
                            valid = s.length > 6
                            return valid
                        }
                    })

                }

                override fun onClose() {
                    binding.blur.visibility = View.GONE
                }
            }
        )
        showSheetBlur(binding.mainView, binding.blur, sheet1)
    }


    private fun downloadPdf(invoiceFile: String?) {
        Permissions.check(this, arrayOf(
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
                            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
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


    private var mixtureAdpater: RVAdapter<ProductDetail, UpdateCreateMixBinding>? = null
    var moqStep: Int = 0
    var moqSteper: Int = 0
    private fun showComboSheet(m: Product?) {
        m?.let { mixBox ->
            sheet2 = BaseCloseSheet(
                R.layout.bs_create_mix,
                object : BaseCloseSheet.ClickListener<BsCreateMixBinding> {
                    override fun onClick(view: View) {
                        when (view.id) {
                            R.id.tv_update -> {
                                sheet2?.dismissAllowingStateLoss()
                                if (mixBox.products?.isNotEmpty() == true) {
                                    val product = ArrayList<ProductEditData>()
                                    for (index in mixBox.products) {
                                        if (index.quantity > 0) {
                                            index.product?.id?.let { id ->
                                                product.add(
                                                    ProductEditData(
                                                        id,
                                                        index.quantity,
                                                        mixBox.id
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    binding.order?.id?.let {
                                        viewmodel.editProductQty(
                                            it,
                                            EditProductRequest(product)
                                        )
                                    }

                                }
                            }

                        }
                    }

                    override fun onViewCreated(b: BsCreateMixBinding) {
                        mixtureAdpater =
                            RVAdapter(
                                R.layout.update_create_mix,
                                BR.bean,
                                object : RVAdapter.Callback<ProductDetail> {
                                    override fun onItemClick(v: View, m: ProductDetail) {

                                    }

                                    override fun onItemClick(v: View, m: ProductDetail, pos: Int) {
                                        when (v.id) {
                                            R.id.card_minus -> {
                                                if (m.quantity > 0) {
                                                    m.quantity = m.quantity - 1
                                                    mixtureAdpater?.notifyItemChanged(pos)
                                                    variantAdded(b)
                                                }
                                            }
                                            R.id.card_plus -> {
                                                m.quantity = m.quantity + 1
                                                mixtureAdpater?.notifyItemChanged(pos)
                                                variantAdded(b)
                                            }
                                        }
                                    }
                                })
                        b.rvMixture.adapter = mixtureAdpater
                        mixtureAdpater?.setDataList(mixBox.products)
                        mixtureAdpater?.setDataList(mixBox.products)
                        if (mixBox.products?.isNotEmpty() == true) {
                            mixBox.products[0].product?.retailerMoq?.let {
                                moqStep = it
                                moqSteper = it
                            }
                            variantAdded(b)
                        }
                    }

                    override fun onClose() {
                        binding.blur.visibility = View.GONE

                    }
                })
            showSheetBlur(binding.mainView, binding.blur, sheet2)
        }

    }


    private fun variantAdded(b: BsCreateMixBinding) {
        var finalComboCount = 0
        var count = 1
        var check = false
        mixtureAdpater?.getDataList()?.let {
            for (index in it.indices) {
                finalComboCount += it[index].quantity
            }

            if (!check) {
                val c = finalComboCount / moqStep
                if (c > 0 && finalComboCount % moqStep == 0) {
                    moqStep = c * it[0].product?.retailerMoq!!
                    moqSteper = it[0].product?.retailerMoq!!
                    count = c
                }
            }

            val dividend: Float = finalComboCount.toFloat() / moqSteper.toFloat()
            val reminder: Float = finalComboCount.toFloat() % moqSteper.toFloat()
            var step = 0
            if (reminder == 0f) {
                b.tvUpdate.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.orange
                    )
                )
                b.tvUpdate.isEnabled = true
                step = if (finalComboCount == 0) 1 else (dividend).toInt()

                b.tvMix.setTextColor(ContextCompat.getColor(this, R.color.colorSuccess))

            } else {
                b.tvUpdate.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.medium_gray
                    )
                )
                b.tvUpdate.isEnabled = false
                step = (dividend + 1).toInt()
                b.tvMix.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.secondary_red
                    )
                )
            }
            b.tvMix.text =
                "MOQ : " + finalComboCount + "/" + (step * moqSteper)

        }
    }


}