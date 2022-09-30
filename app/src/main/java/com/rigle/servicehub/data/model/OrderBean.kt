package com.rigle.servicehub.data.model

import com.google.gson.annotations.SerializedName


data class OrderBean(
    @SerializedName("assigned_runner")
    val assignedRunner: Runner?,
    @SerializedName("buyer")
    val buyer: Buyer?,
    @SerializedName("cart_user")
    val cartUser: String?,
    @SerializedName("challan_file")
    val challanFile: String?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("delivery_date")
    val deliveryDate: String?,
    @SerializedName("earned_coins")
    val earnedCoins: Int?,
    @SerializedName("final_amount")
    val finalAmount: Float?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("invoice_no")
    val invoiceNo: String?,
    @SerializedName("invoice_file")
    val invoiceFile: String?,
    @SerializedName("is_cart")
    val isCart: Boolean?,
    @SerializedName("paid_amount")
    val paidAmount: Float?,
    @SerializedName("pending_amount")
    val pendingAmount: Float?,
    @SerializedName("product_amount")
    val productAmount: Float?,
    @SerializedName("redeemed_coins")
    val redeemedCoins: Int?,
    @SerializedName("seller")
    val seller: Any?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("total_discount_amount")
    val totalDiscountAmount: Float?,
    @SerializedName("update_url")
    val updateUrl: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("products")
    val products: List<Product>?,
    @SerializedName("statuses")
    val orderStatus: List<OrderStatus>?
) {
    fun getLastStatus(): OrderStatus? {
        if (orderStatus != null && orderStatus.isNotEmpty()) {
            return orderStatus.last()
        }
        return null
    }
}

data class Buyer(
    @SerializedName("account_status")
    val accountStatus: String?,
    @SerializedName("address_line")
    val addressLine: String?,
    @SerializedName("admin")
    val admin: Admin?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("full_address")
    val fullAddress: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("locality")
    val locality: String?,
    @SerializedName("logo")
    val logo: String?,
    @SerializedName("long")
    val long: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("pincode")
    val pincode: String?,
    @SerializedName("short_address")
    val shortAddress: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("update_url")
    val updateUrl: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)
