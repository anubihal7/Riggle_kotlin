package com.rigle.servicehub.data.model


import com.google.gson.annotations.SerializedName

data class OrderStatus(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("order")
    val order: Int?,
    @SerializedName("remark")
    val remark: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("user")
    val user: Int?,
    @SerializedName("visit_date")
    val visitDate: Any?
)