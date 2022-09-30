package com.rigle.servicehub.ui.inventory

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.Inventory
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {

    val obrInventory: SingleRequestEvent<List<Inventory>> = SingleRequestEvent()

    fun getInventory() {
        baseRepo.getInventory()
            .apiSubscription(obrInventory).addToCompositeDisposable()
    }

}