package com.rigle.servicehub.ui.orders

import androidx.databinding.ObservableField
import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.LocalMedia
import com.rigle.servicehub.data.model.helper.UploadBean
import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import com.rigle.servicehub.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val baseRepo: BaseRepo,
    private val sharedPrefManager: SharedPrefManager
) :
    BaseViewModel() {
    val field_name = ObservableField<String>()
    val field_address = ObservableField<String>()
    val field_fcci = ObservableField<String>()
    val field_gst = ObservableField<String>()

    val obrUpdate: SingleRequestEvent<UploadBean> = SingleRequestEvent()

    fun updateProfile(images: List<LocalMedia>?, map: Map<String, String>) {
     /*   val id = sharedPrefManager.getCurrentUser()?.service_hub?.id ?: return
        baseRepo.updateBusinessProfile(id.toString(),images,map)
            .mediaSubscription(obrUpdate).addToCompositeDisposable()*/
    }

}