package com.rigle.servicehub.data.model

data class EditProductRequest(
    val products: List<ProductEditData>
)

data class ProductEditData(
    val product: Int,
    val quantity: Int,
    val product_combo: Int?
)