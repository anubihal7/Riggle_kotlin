package com.rigle.servicehub.data.model

data class StockResponse(
    val code: String,
    val company: Int,
    val created_at: String,
    val doc_id: String,
    val id: Int,
    val image: String?,
    val is_active: Boolean,
    val name: String?,
    val products: List<Product>,
    val update_url: String,
    val updated_at: String
)