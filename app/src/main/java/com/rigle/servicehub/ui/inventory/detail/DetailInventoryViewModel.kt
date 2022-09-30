package com.rigle.servicehub.ui.inventory.detail

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.CartResponse
import com.rigle.servicehub.data.model.Stock
import com.rigle.servicehub.data.network.base.PageResponse
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class DetailInventoryViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {

    val obrStock: SingleRequestEvent<PageResponse<Stock>> = SingleRequestEvent()
    val obrCart: SingleRequestEvent<CartResponse> = SingleRequestEvent()

    fun getStock(id: String) {
        baseRepo.getStockById(id)
            .apiSubscription(obrStock).addToCompositeDisposable()
    }

    val obrUpdate: SingleRequestEvent<JsonElement> = SingleRequestEvent()
    val obrUpdateQty: SingleRequestEvent<JsonElement> = SingleRequestEvent()
    val obrSubmit: SingleRequestEvent<JsonElement> = SingleRequestEvent()
    fun updateStockProduct(id: String, status: Boolean) {
        val map = hashMapOf<String, String>()
        map["is_active"] = status.toString()
        baseRepo.updateStockProduct(id, map)
            .apiSubscription(obrUpdate).addToCompositeDisposable()
    }

    fun updateStockQty(orderId: String, jsonObject: JSONObject) {
        baseRepo.updateStockQty(orderId, jsonObject)
            .apiSubscription(obrUpdateQty).addToCompositeDisposable()
    }

    fun submitOrder(jsonObject: JSONObject) {
        baseRepo.submitOrder(jsonObject)
            .apiSubscription(obrSubmit).addToCompositeDisposable()
    }

    fun getCart(jsonObject:  Map<String,String>) {
        baseRepo.getCart(jsonObject).retry()
            .apiSubscription(obrCart).addToCompositeDisposable()
    }
}