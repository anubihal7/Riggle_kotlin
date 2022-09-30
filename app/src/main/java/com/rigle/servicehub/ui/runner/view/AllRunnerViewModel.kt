package com.rigle.servicehub.ui.runner.view

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.Runner
import com.rigle.servicehub.data.network.base.PageResponse
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllRunnerViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val obrRunner: SingleRequestEvent<PageResponse<Runner>> = SingleRequestEvent()

    fun getRunner() {
        baseRepo.getRunner()
            .apiSubscription(obrRunner).addToCompositeDisposable()
    }

}