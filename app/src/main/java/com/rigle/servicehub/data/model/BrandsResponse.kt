package com.rigle.servicehub.data.model


import com.google.gson.annotations.SerializedName

data class BrandsResponse(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("next")
    val next: Any?,
    @SerializedName("previous")
    val previous: Any?,
    @SerializedName("results")
    val results: List<Brand>?
)
