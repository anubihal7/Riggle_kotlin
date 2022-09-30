package com.rigle.servicehub.ui.account.edit

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.MyAccountResponse
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditAccountViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val obrMyAccount = SingleRequestEvent<MyAccountResponse>()

    fun getMyAccount() {
        val id = sharedPrefManager.getCurrentUser()?.company?.id ?: return
        baseRepo.myAccount(id.toString())
            .apiSubscription(obrMyAccount).addToCompositeDisposable()
    }
}