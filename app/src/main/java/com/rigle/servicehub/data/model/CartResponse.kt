package com.rigle.servicehub.data.model


import com.google.gson.annotations.SerializedName

data class CartResponse(
    @SerializedName("assigned_runner")
    val assignedRunner: Any?,
    @SerializedName("buyer")
    val buyer: Int?,
    @SerializedName("cart_user")
    val cartUser: Int?,
    @SerializedName("challan_file")
    val challanFile: Any?,
    @SerializedName("code")
    val code: Any?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("credit_otp")
    val creditOtp: Any?,
    @SerializedName("delivery_date")
    val deliveryDate: Any?,
    @SerializedName("earned_coins")
    val earnedCoins: Int?,
    @SerializedName("final_amount")
    val finalAmount: Double?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("invoice_file")
    val invoiceFile: Any?,
    @SerializedName("invoice_no")
    val invoiceNo: Any?,
    @SerializedName("is_cart")
    val isCart: Boolean?,
    @SerializedName("is_credit_enabled")
    val isCreditEnabled: Boolean?,
    @SerializedName("paid_amount")
    val paidAmount: Double?,
    @SerializedName("pending_amount")
    val pendingAmount: Double?,
    @SerializedName("product_amount")
    val productAmount: Double?,
    @SerializedName("products")
    val products: List<Any?>?,
    @SerializedName("redeemed_coins")
    val redeemedCoins: Int?,
    @SerializedName("seller")
    val seller: Any?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("total_discount_amount")
    val totalDiscountAmount: Double?,
    @SerializedName("update_url")
    val updateUrl: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("visit_date")
    val visitDate: Any?
)