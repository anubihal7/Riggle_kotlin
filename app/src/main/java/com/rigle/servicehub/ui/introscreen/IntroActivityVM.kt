package com.rigle.servicehub.ui.introscreen

import com.rigle.servicehub.data.repository.BaseRepo
import com.rigle.servicehub.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroActivityVM @Inject constructor(
    private val baseRepo: BaseRepo
) : BaseViewModel() {

}