package com.rigle.servicehub.data.model

import com.google.gson.annotations.SerializedName

data class Brand(
    @SerializedName("code")
    val code: String?,
    @SerializedName("company")
    val company: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("doc_id")
    val docId: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("update_url")
    val updateUrl: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    var checked: Boolean = false
)
