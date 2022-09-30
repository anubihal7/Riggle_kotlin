package com.rigle.servicehub.ui.runner.detail

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.RunnerResponse
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RunnerDetailViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val obrRunner: SingleRequestEvent<RunnerResponse> = SingleRequestEvent()
    fun getRunnerDetail(time: Date, runnerId: String) {
        /*2022-07-01*/
        val map = hashMapOf<String, String>()
        map["date"] = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(time)
        map["runner_id"] = runnerId
        val id = sharedPrefManager.getCurrentUser()?.company?.id ?: return
        baseRepo.runnersSettlementDetail(id.toString(), map)
            .apiSubscription(obrRunner).addToCompositeDisposable()
    }
}