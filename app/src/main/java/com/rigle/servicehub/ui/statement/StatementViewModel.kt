package com.rigle.servicehub.ui.statement

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatementViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val obrData = SingleRequestEvent<JsonElement>()

    fun getSettlement(map: Map<String, String>) {
        val id = sharedPrefManager.getCurrentUser()?.company?.id ?: return
        baseRepo.getSettlements(id.toString(), map)
            .apiSubscription(obrData).addToCompositeDisposable()
    }
}