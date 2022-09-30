package com.rigle.servicehub.ui.welcome

import com.rigle.servicehub.data.model.RigleConstantsResponse
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeActivityVM @Inject constructor(
    private val baseRepo: BaseRepo
) :
    BaseViewModel() {
    val obrConstants = SingleRequestEvent<RigleConstantsResponse>()

    fun getConstants() {
        baseRepo.getRigleConstants()
            .retry()
            .apiSubscription(obrConstants).addToCompositeDisposable()
    }

}