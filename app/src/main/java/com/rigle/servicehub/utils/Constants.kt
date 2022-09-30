package com.rigle.servicehub.utils

import com.rigle.servicehub.ui.base.location.LocationHandler

/**
 * Created by shank_000 on 10/3/2018.
 */
interface Constants {
    companion object {
        const val STAGGING_URL: String = "https://stag.api.riggleapp.in/api/v1/"
        const val LIVE_URL: String = "https://api.riggleapp.in/api/v1/"
        const val BASE_URL: String = STAGGING_URL


        const val USERNAME = "7506175283"
        const val PASSWORD = "7506175283"
        const val DEVICE_TYPE = "2"

        const val STATUS_PENDING = "pending"
        const val STATUS_CONFIRMED = "confirmed"
        const val STATUS_COMPLETED = "completed"
        const val STATUS_CANCELLED = "cancelled"
        const val STATUS_OUTSTANDING = "outstanding"
        const val STATUS_DELIVERED = "delivered"
        const val ACCOUNT_STATUS_PENDING = "pending"

        const val OFFER_FIXED = "fixed"
        const val OFFER_PERCENT = "percentage"
        const val OFFER_ONCE = "only_once"
        const val OFFER_ONCE_A_DAY = "once_a_day"
        var locationHandler: LocationHandler? = null

    }
}