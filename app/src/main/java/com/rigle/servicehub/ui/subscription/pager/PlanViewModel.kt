package com.rigle.servicehub.ui.subscription.pager

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.PaymentResponse
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val obrBuy: SingleRequestEvent<PaymentResponse> = SingleRequestEvent()
    fun buyPlan(id:String,parm: JSONObject) {
        baseRepo.initiatePlan(id,parm)
            .apiSubscription(obrBuy).addToCompositeDisposable()
    }
}