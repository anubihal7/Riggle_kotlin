package com.rigle.servicehub.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RunnerResponse(
    @SerializedName("cash_total")
    val cashTotal: Int?,
    @SerializedName("cash_trips")
    val cashTrips: Int?,
    @SerializedName("cheque_total")
    val chequeTotal: Int?,
    @SerializedName("cheque_trips")
    val chequeTrips: Int?,
    @SerializedName("pending_total")
    val pendingTotal: Int?,
    @SerializedName("pending_trips")
    val pendingTrips: Int?,
    @SerializedName("redeemed_coins")
    val redeemedCoins: Int?,
    @SerializedName("redeemed_coins_value")
    val redeemedCoinsValue: Int?,
    @SerializedName("riggle_credit_total")
    val riggleCreditTotal: Int?,
    @SerializedName("riggle_credit_trips")
    val riggleCreditTrips: Int?,
    @SerializedName("runner")
    val runner: Runner?,
    @SerializedName("total_settlement")
    val totalSettlement: Float?,
    @SerializedName("trips")
    val trips: Int?,
    @SerializedName("upi_total")
    val upiTotal: Int?,
    @SerializedName("upi_trips")
    val upiTrips: Int?
):Parcelable