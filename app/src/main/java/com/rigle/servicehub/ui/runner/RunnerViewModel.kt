package com.rigle.servicehub.ui.runner

import androidx.databinding.ObservableField
import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RunnerViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val fieldName = ObservableField<String>()
    val fieldPhone = ObservableField<String>()

    val obrUpdate: SingleRequestEvent<JsonElement> = SingleRequestEvent()

    fun addRunner(parm: JSONObject) {
        baseRepo.addUser(parm)
            .apiSubscription(obrUpdate).addToCompositeDisposable()
    }

}