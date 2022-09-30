package com.rigle.servicehub.ui.dashboard

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.Company
import com.rigle.servicehub.data.model.DashboardResponse
import com.rigle.servicehub.data.model.PlanResponse
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val obrDashboard: SingleRequestEvent<DashboardResponse> = SingleRequestEvent()
    val obrCompany: SingleRequestEvent<Company> = SingleRequestEvent()
    val obrPlan: SingleRequestEvent<PlanResponse> = SingleRequestEvent()

    fun dashboard() {
        val id = sharedPrefManager.getCurrentUser()?.company?.id ?: return
        baseRepo.dashboard(id.toString())
            .apiSubscription(obrDashboard).addToCompositeDisposable()
    }

    fun getCompany() {
        val id = sharedPrefManager.getCurrentUser()?.company?.id ?: return
        baseRepo.getCompany(id.toString())
            .apiSubscription(obrCompany).addToCompositeDisposable()
    }

    fun getPlans() {
        val id = sharedPrefManager.getCurrentUser()?.company?.id ?: return
        baseRepo.planData(id.toString())
            .apiSubscription(obrPlan).addToCompositeDisposable()
    }
}
