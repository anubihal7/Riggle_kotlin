package com.rigle.servicehub.ui.base.location

import android.location.Location

interface LocationResultListener {
    fun onLocationRefresh(location: Location)
    fun onOldLocation(location: Location)
    fun onRefreshStart()
    fun onRefreshStop()
    fun onError(error: String?)

}