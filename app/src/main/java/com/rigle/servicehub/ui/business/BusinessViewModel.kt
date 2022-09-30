package com.rigle.servicehub.ui.business

import androidx.databinding.ObservableField
import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.Company
import com.rigle.servicehub.data.model.LocalMedia
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import com.rigle.servicehub.utils.extension.apiSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BusinessViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val fieldName = ObservableField<String>()
    val fieldGst = ObservableField<String>()
    val fieldProof = ObservableField<String>()
    val fieldAddress = ObservableField<String>()
    val fieldLocality = ObservableField<String>()
    val fieldPincode = ObservableField<String>()
    val fieldLandmark = ObservableField<String>()
    val fieldCity = ObservableField<String>()
    val fieldState = ObservableField<String>()


    val obrUpdate = SingleRequestEvent<Company>()

    fun updateBusiness(map: Map<String, String>, list: List<LocalMedia>) {
        val id = sharedPrefManager.getCurrentUser()?.company?.id ?: return
        baseRepo.updateBusinessProfile(id.toString(), map, list)
            .apiSubscription(obrUpdate).addToCompositeDisposable()
    }

}