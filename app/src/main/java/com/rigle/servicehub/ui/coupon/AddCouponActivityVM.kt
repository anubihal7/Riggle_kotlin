package com.rigle.servicehub.ui.coupon

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.BrandsResponse
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class AddCouponActivityVM @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) : BaseViewModel() {

    val obrBrands: SingleRequestEvent<BrandsResponse> = SingleRequestEvent()
    val obrAddOffer: SingleRequestEvent<JsonElement> = SingleRequestEvent()

    fun getBrands() {
        baseRepo.getBrands()
            .apiSubscription(obrBrands).addToCompositeDisposable()
    }

    fun addOffer(parm: JSONObject) {
        baseRepo.addOffer(parm)
            .apiSubscription(obrAddOffer).addToCompositeDisposable()
    }
}