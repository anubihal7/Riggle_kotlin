package com.rigle.servicehub.ui.sales

import androidx.databinding.ObservableField
import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.SalesPerson
import com.rigle.servicehub.data.network.base.PageResponse
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MangeSalesViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val fieldName = ObservableField<String>()
    val fieldPhone = ObservableField<String>()

    val obrSalesPerson: SingleRequestEvent<PageResponse<SalesPerson>> = SingleRequestEvent()

    fun getSalesPerson() {
        baseRepo.getSalesPerson()
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