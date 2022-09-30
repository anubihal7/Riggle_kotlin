package com.rigle.servicehub.ui.main

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
)  : BaseViewModel() {
    val obrDashboard: SingleRequestEvent<JsonElement> = SingleRequestEvent()


    fun ping() {
        baseRepo.authPing()
            .apiSubscription(obrDashboard).addToCompositeDisposable()
    }

}