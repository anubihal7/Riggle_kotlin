package com.rigle.servicehub.ui.login

import androidx.databinding.ObservableField
import com.rigle.servicehub.data.model.UserResponse
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginActivityVM @Inject constructor(
    private val baseRepo: BaseRepo
) : BaseViewModel() {
    val obrOtp: SingleRequestEvent<JsonElement> = SingleRequestEvent()
    val obrverify: SingleRequestEvent<UserResponse> = SingleRequestEvent()
    val field_otp = ObservableField<String>()
    val field_phone = ObservableField<String>()

    fun getOtp(jsonObject: JSONObject) {
        baseRepo.getOtp(jsonObject)
            .apiSubscription(obrOtp).addToCompositeDisposable()
    }

    fun verifyOtp(jsonObject: JSONObject) {
        baseRepo.verifyOtp(jsonObject)
            .apiSubscription(obrverify).addToCompositeDisposable()
    }
}