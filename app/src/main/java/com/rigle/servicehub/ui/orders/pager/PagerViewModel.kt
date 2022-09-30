package com.rigle.servicehub.ui.orders.pager

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.OrderBean
import com.rigle.servicehub.data.model.Runner
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
class PagerViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val obrAssignRunner: SingleRequestEvent<JsonElement> = SingleRequestEvent()
    val obrData: SingleRequestEvent<PageResponse<OrderBean>> = SingleRequestEvent()
    val obrRunner: SingleRequestEvent<PageResponse<Runner>> = SingleRequestEvent()
    val obrConfirm: SingleRequestEvent<JsonElement> = SingleRequestEvent()
    fun runnerConfirm(id: String,runnerId: String) {
        val map = hashMapOf<String, String>()
        map["assigned_runner"] =runnerId
        baseRepo.confirmOrder(id, map)
            .apiSubscription(obrConfirm).addToCompositeDisposable()
    }

    fun assignRunner(orderId: String, parm: JSONObject) {
        baseRepo.assignRunner(orderId, parm)
            .apiSubscription(obrAssignRunner).addToCompositeDisposable()
    }

    fun getData(map: Map<String, String>) {
        baseRepo.getOrder(map)
            .apiSubscription(obrData).addToCompositeDisposable()
    }

    fun getRunner() {
        baseRepo.getRunner()
            .apiSubscription(obrRunner).addToCompositeDisposable()
    }
}