package com.rigle.servicehub.ui.orders.detail

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.EditProductRequest
import com.rigle.servicehub.data.model.OrderBean
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class DetailOrderViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val obrData: SingleRequestEvent<OrderBean> = SingleRequestEvent()
    val obrCancel: SingleRequestEvent<JsonElement> = SingleRequestEvent()
    val obrUpdate: SingleRequestEvent<JsonElement> = SingleRequestEvent()

    fun getData(orderId: String, map: Map<String, String>) {
        baseRepo.getOrder(orderId, map)
            .apiSubscription(obrData).addToCompositeDisposable()
    }

    fun cancelOrder(id: String, parm: JSONObject) {
        baseRepo.cancelOrder(id, parm)
            .apiSubscription(obrCancel).addToCompositeDisposable()
    }
    fun editProductQty(orderId: Int, editProductRequest: EditProductRequest) {
        baseRepo.updateProducts(orderId.toString(), editProductRequest)
            .apiSubscription(obrUpdate).addToCompositeDisposable()
    }
}