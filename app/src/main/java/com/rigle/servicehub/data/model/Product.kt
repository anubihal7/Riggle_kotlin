package com.rigle.servicehub.data.model

import com.google.gson.annotations.SerializedName


data class Product(
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("coins")
    val coins: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("free_product")
    val freeProduct: Any?,
    @SerializedName("free_product_quantity")
    val freeProductQuantity: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("order")
    val order: Int?,
    @SerializedName("ordered_quantity")
    val orderedQuantity: Int?,
    @SerializedName("original_rate")
    val originalRate: Double?,
    @SerializedName("product")
    val product: ProductDetail?,
    @SerializedName("products")
    val products: List<ProductDetail>?,
    @SerializedName("product_combo")
    val productCombo: Any?,
    @SerializedName("quantity")
    var quantity: Int,
    @SerializedName("rate")
    val rate: Double?,
    @SerializedName("update_url")
    val updateUrl: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("name")
    val name: String?
) {

    fun getRealProductName(): String? {
        if (name.isNullOrEmpty()) {
            return product?.productName
        }
        return name
    }
}

data class ProductDetail(
    @SerializedName("base_quantity")
    val baseQuantity: Int?,
    @SerializedName("base_rate")
    val baseRate: Double?,
    @SerializedName("rate")
    val rate: Double?,
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
    @SerializedName("name")
    val productName: String?,
    @SerializedName("normalized_weight")
    val normalizedWeight: Double?,
    @SerializedName("retailer_moq")
    val retailerMoq: Int?,
    @SerializedName("sub_category")
    val subCategory: String?,
    @SerializedName("update_url")
    val updateUrl: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("quantity")
    var quantity: Int,
    val product: ProductDetail?
)
