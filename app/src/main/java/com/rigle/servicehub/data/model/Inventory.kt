package com.rigle.servicehub.data.model


import com.google.gson.annotations.SerializedName

data class Inventory(
    @SerializedName("brand")
    val brand: Brand?,
    @SerializedName("total_final_quantity")
    val totalFinalQuantity: Int?,
    @SerializedName("total_purchase_quantity")
    val totalPurchaseQuantity: Int?,
    @SerializedName("total_quantity")
    val totalQuantity: Int?,
    @SerializedName("total_sales_quantity")
    val totalSalesQuantity: Int?
)