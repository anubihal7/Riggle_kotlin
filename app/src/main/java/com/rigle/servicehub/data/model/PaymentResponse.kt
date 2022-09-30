package com.rigle.servicehub.data.model


import com.google.gson.annotations.SerializedName

data class PaymentResponse(
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("amount_paid")
    val amountPaid: Double?,
    @SerializedName("cancelled_at")
    val cancelledAt: Any?,
    @SerializedName("confirmed_at")
    val confirmedAt: Any?,
    @SerializedName("content_id")
    val contentId: Int?,
    @SerializedName("content_type")
    val contentType: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("initiated_at")
    val initiatedAt: Any?,
    @SerializedName("is_partial_allowed")
    val isPartialAllowed: Boolean?,
    @SerializedName("location")
    val location: Any?,
    @SerializedName("payment_link")
    val paymentLink: String?,
    @SerializedName("payment_link_id")
    val paymentLinkId: String?,
    @SerializedName("payment_mode")
    val paymentMode: Any?,
    @SerializedName("purpose")
    val purpose: String?,
    @SerializedName("receipt")
    val receipt: Any?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("txn_id")
    val txnId: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("update_url")
    val updateUrl: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)