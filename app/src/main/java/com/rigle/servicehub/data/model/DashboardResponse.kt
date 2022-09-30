package com.rigle.servicehub.data.model

import com.google.gson.annotations.SerializedName


data class DashboardResponse(
    @SerializedName("coins_balance")
    val coinsBalance: Int?,
    @SerializedName("credit")
    val credit: Item?,
    @SerializedName("outstanding")
    val outstanding: Item?,
    @SerializedName("pending")
    val pending: Item?,
    @SerializedName("revenues")
    val revenues: Revenues?
) {
    data class Revenues(
        @SerializedName("Daily")
        val daily: List<Item>?,
        @SerializedName("Monthly")
        val monthly: List<Item>?,
        @SerializedName("Weekly")
        val weekly: List<Item>?,
        @SerializedName("Yearly")
        val yearly: List<Item>?
    ) {
        data class Item(
            @SerializedName("name")
            val name: String?,
            @SerializedName("value")
            val value: Int?
        )


    }

    data class Item(
        @SerializedName("amount")
        val amount: Float?,
        @SerializedName("count")
        val count: Int?
    )
}