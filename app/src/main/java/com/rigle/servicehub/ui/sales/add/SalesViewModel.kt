package com.rigle.servicehub.ui.sales.add

import androidx.databinding.ObservableField
import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.BrandsResponse
import com.rigle.servicehub.data.model.SalesPerson
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val fieldName = ObservableField<String>()
    val fieldPhone = ObservableField<String>()

    val obrUpdate: SingleRequestEvent<JsonElement> = SingleRequestEvent()
    val obrBrands: SingleRequestEvent<BrandsResponse> = SingleRequestEvent()
    val obrSalesPerson = SingleRequestEvent<SalesPerson>()
    fun addSalesPerson(parm: JSONObject) {
        baseRepo.addUser(parm)
            .apiSubscription(obrUpdate).addToCompositeDisposable()
    }
    fun updateSalesPerson(id:String,parm: JSONObject) {
        baseRepo.updateSalesPerson(id,parm)
            .apiSubscription(obrUpdate).addToCompositeDisposable()
    }

    fun getBrands() {
        baseRepo.getBrands()
            .apiSubscription(obrBrands).addToCompositeDisposable()
    }


    fun getSalesPerson(runnerId: String) {
        baseRepo.getSalesPerson(runnerId)
            .apiSubscription(obrSalesPerson).addToCompositeDisposable()
    }
}