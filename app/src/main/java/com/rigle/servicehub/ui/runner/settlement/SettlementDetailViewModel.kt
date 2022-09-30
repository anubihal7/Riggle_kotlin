package com.rigle.servicehub.ui.runner.settlement

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.*
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettlementDetailViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val obrRunner = SingleRequestEvent<Runner>()

    fun getRunner(runnerId: String) {
        baseRepo.getRunner(runnerId)
            .apiSubscription(obrRunner).addToCompositeDisposable()
    }
}