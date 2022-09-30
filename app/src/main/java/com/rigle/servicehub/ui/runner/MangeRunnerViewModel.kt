package com.rigle.servicehub.ui.runner

import androidx.databinding.ObservableField
import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.RunnerResponse
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MangeRunnerViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val fieldName = ObservableField<String>()
    val fieldPhone = ObservableField<String>()
    val obrRunner: SingleRequestEvent<List<RunnerResponse>> = SingleRequestEvent()
    val obrUpdate: SingleRequestEvent<JsonElement> = SingleRequestEvent()

    fun getRunner(time: Date) {
        /*2022-07-01*/
        val map = hashMapOf<String, String>()
        map["date"] = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(time)
        val id = sharedPrefManager.getCurrentUser()?.company?.id ?: return
        baseRepo.runnersSettlement(id.toString(), map)
            .apiSubscription(obrRunner).addToCompositeDisposable()
    }

    fun updateRunner(id: String, status: Boolean) {
        val map = hashMapOf<String, String>()
        map["is_active"] = status.toString()
        baseRepo.updateUserInfo(id, map)
            .apiSubscription(obrUpdate).addToCompositeDisposable()
    }
}