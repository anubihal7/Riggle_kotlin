package com.rigle.servicehub.ui.owner

import androidx.databinding.ObservableField
import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.Company
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class OwnerViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val field_fullname = ObservableField<String>()
    val field_co_name = ObservableField<String>()
    val field_mobile = ObservableField<String>()

    val obrUpdate: SingleRequestEvent<Company> = SingleRequestEvent()
    val obrOwner: SingleRequestEvent<JsonElement> = SingleRequestEvent()

    fun updateUser(fullName:String) {
        val id = sharedPrefManager.getCurrentUser()?.id ?: return
        baseRepo.updateUser(id.toString(),fullName)
            .apiSubscription(obrUpdate).addToCompositeDisposable()
    }

    fun addUser(parm: JSONObject) {
        baseRepo.addUser(parm)
            .apiSubscription(obrOwner).addToCompositeDisposable()
    }
}