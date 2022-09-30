package com.rigle.servicehub.ui.account

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.*
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val obrMyAccount = SingleRequestEvent<MyAccountResponse>()
    val obrUpdate = SingleRequestEvent<Company>()

    fun getMyAccount() {
        val id = sharedPrefManager.getCurrentUser()?.company?.id ?: return
        baseRepo.myAccount(id.toString())
            .apiSubscription(obrMyAccount).addToCompositeDisposable()
    }


    fun updateQrcode(qrCode: JSONObject) {
        val id = sharedPrefManager.getCurrentUser()?.company?.id ?: return
        baseRepo.updateQrCode(id.toString(),qrCode.toString())
            .apiSubscription(obrUpdate).addToCompositeDisposable()
    }
}