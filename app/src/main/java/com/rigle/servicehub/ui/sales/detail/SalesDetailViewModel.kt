package com.rigle.servicehub.ui.sales.detail

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.*
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SalesDetailViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val obrSalesPerson = SingleRequestEvent<SalesPerson>()

    fun getSalesPerson(runnerId: String) {
        baseRepo.getSalesPerson(runnerId)
            .apiSubscription(obrSalesPerson).addToCompositeDisposable()
    }

    val obrUpdate: SingleRequestEvent<JsonElement> = SingleRequestEvent()
    fun updateUser(id: String, status: Boolean) {
        val map = hashMapOf<String, String>()
        map["is_active"] = status.toString()
        baseRepo.updateUserInfo(id, map)
            .apiSubscription(obrUpdate).addToCompositeDisposable()
    }
}