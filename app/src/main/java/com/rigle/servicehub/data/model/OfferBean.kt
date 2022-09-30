package com.rigle.servicehub.data.model


import com.google.gson.annotations.SerializedName

data class OfferBean(
    @SerializedName("available_quantity")
    val availableQuantity: Int?,
    @SerializedName("brand")
    val brand: Int?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("company")
    val company: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("discount_amount")
    val discountAmount: Double?,
    @SerializedName("discount_type")
    val discountType: String?,
    @SerializedName("expiry")
    val expiry: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("min_amount")
    val minAmount: Double?,
    @SerializedName("ordered_quantity")
    val orderedQuantity: Int?,
    @SerializedName("payment_amount")
    val paymentAmount: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("update_url")
    val updateUrl: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("usage_type")
    val usageType: String?
)