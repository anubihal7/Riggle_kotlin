package com.rigle.servicehub.ui.info

import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.SettleBeanItem
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardInfoActivityVM @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) : BaseViewModel() {

    val obrData: SingleRequestEvent<List<SettleBeanItem>> = SingleRequestEvent()

    fun getData() {
      /*  val id = sharedPrefManager.getCurrentUser()?.service_hub?.id ?: return
        baseRepo.settlementSummary(id.toString())
            .apiSubscription(obrData).addToCompositeDisposable()*/
    }
}