package com.rigle.servicehub.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class PlanResponse(
    @SerializedName("add_product_form_link")
    val addProductFormLink: String?,
    @SerializedName("subscribe_link")
    val subscribeLink: String?,
    @SerializedName("subscription_data")
    val subscriptionData: List<SubscriptionData>
) {
    @Parcelize
    data class SubscriptionData(
        @SerializedName("duration")
        val duration: String?,
        @SerializedName("duration_id")
        val durationId: Int?,
        @SerializedName("feature_title")
        val featureTitle: String?,
        @SerializedName("features")
        val features: List<String>,
        @SerializedName("plans")
        val plans: List<Plan>
    ) : Parcelable {
        @Parcelize
        data class Plan(
            @SerializedName("description")
            val description: String?,
            @SerializedName("is_recommended")
            val isRecommended: Boolean?,
            @SerializedName("monthly_amount")
            val monthlyAmount: String?,
            @SerializedName("plan_id")
            val planId: String?,
            @SerializedName("total_amount")
            val totalAmount: String?
        ) : Parcelable
    }
}