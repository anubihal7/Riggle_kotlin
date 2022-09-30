package com.rigle.servicehub.data.network.base

import com.google.gson.annotations.SerializedName

data class PageResponse<T>(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("next")
    val next: Any?,
    @SerializedName("previous")
    val previous: Any?,
    @SerializedName("results")
    val results: List<T>?
)
