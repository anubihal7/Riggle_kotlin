package com.rigle.servicehub.ui.subscription

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.PlanResponse
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val obrPlan: SingleRequestEvent<PlanResponse> = SingleRequestEvent()
    fun getPlans() {
        val id = sharedPrefManager.getCurrentUser()?.company?.id ?: return
        baseRepo.planData(id.toString())
            .apiSubscription(obrPlan).addToCompositeDisposable()
    }

}