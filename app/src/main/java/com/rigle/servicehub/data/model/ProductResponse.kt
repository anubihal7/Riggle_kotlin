package com.rigle.servicehub.data.model

data class ProductResponse(
    val count: Int?,
    val next: Any?,
    val previous: Any?,
    val results: List<ProductResult>?
)

data class ProductResult(
    var base_quantity: Int,
    val base_unit: Any?,
    val brand: Int?,
    val cart_data: CartData?,
    val code: String?,
    val coins: Int?,
    val combo_products: List<ComboProduct>?,
    val created_at: String?,
    val delivery_tat_days: Int?,
    val description: String?,
    val id: Int?,
    val is_active: Boolean?,
    val mrp: Double?,
    val name: String?,
    val normalized_weight: Double?,
    val retailer_moq: Int,
    val retailer_price: Double?,
    val service_by: ServiceBy?,
    val sub_category: Any?,
    val update_url: String?,
    val updated_at: String?,
    var product_quantity: Int = 0
)

class CartData

data class ComboProduct(
    val code: Any?,
    val created_at: String?,
    val id: Int?,
    val is_active: Boolean?,
    val name: String?,
    //val products: List<Product>?,
    val products: List<ProductResult>?,
    val step: Int?,
    val update_url: String?,
    val updated_at: String?
)

data class ServiceBy(
    val address_line: String?,
    val city: String?,
    val code: String?,
    val created_at: String?,
    val full_address: String?,
    val id: Int?,
    val is_active: Boolean?,
    val lat: Any?,
    val locality: String?,
    val logo: Any?,
    val long: Any?,
    val name: String?,
    val pincode: String?,
    val short_address: String?,
    val state: String?,
    val type: String?,
    val update_url: String?,
    val updated_at: String?
)

data class ComboUpdateResponse(
    val amount: Double,
    val created_at: String,
    val free_product: Any,
    val free_product_quantity: Int,
    val id: Int,
    val order: Int,
    val ordered_quantity: Int,
    val original_rate: Double,
    val product: Int,
    val product_combo: Int,
    val quantity: Int,
    val rate: Double,
    val riggle_coins: Int,
    val update_url: String,
    val updated_at: String
)
