package com.rigle.servicehub.data.model


import com.google.gson.annotations.SerializedName

data class Stock(
    @SerializedName("company")
    val company: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("final_quantity")
    val finalQuantity: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("product")
    val product: Product?,
    @SerializedName("purchase_quantity")
    val purchaseQuantity: Int?,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("sales_quantity")
    val salesQuantity: Int?,
    @SerializedName("update_url")
    val updateUrl: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("new_qty")
    var newQty: Int = 0
) {
    data class Product(
        @SerializedName("base_quantity")
        val baseQuantity: Int?,
        @SerializedName("base_unit")
        val baseUnit: Any?,
        @SerializedName("brand")
        val brand: Int?,
        @SerializedName("code")
        val code: String?,
        @SerializedName("coins")
        val coins: Int?,
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("delivery_tat_days")
        val deliveryTatDays: Int?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("is_active")
        val isActive: Boolean?,
        @SerializedName("mrp")
        val mrp: Double?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("normalized_weight")
        val normalizedWeight: Double?,
        @SerializedName("retailer_moq")
        val retailerMoq: Int?,
        @SerializedName("retailer_price")
        val retailerPrice: Double?,
        @SerializedName("sub_category")
        val subCategory: Any?,
        @SerializedName("update_url")
        val updateUrl: String?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        @SerializedName("distributor_moq")
        val distributorMoq: Int?
    )
}