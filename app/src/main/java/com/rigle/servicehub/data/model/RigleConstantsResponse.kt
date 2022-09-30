package com.rigle.servicehub.data.model


import com.google.gson.annotations.SerializedName

data class RigleConstantsResponse(
    @SerializedName("base_units")
    val baseUnits: List<BaseUnit?>?,
    @SerializedName("category_types")
    val categoryTypes: List<CategoryType?>?,
    @SerializedName("company_proof_types")
    val companyProofTypes: List<CompanyProofType>?,
    @SerializedName("offer_types")
    val offerTypes: List<OfferType?>?,
    @SerializedName("order_statuses")
    val orderStatuses: List<OrderStatuse?>?,
    @SerializedName("pack_units")
    val packUnits: List<PackUnit?>?,
    @SerializedName("payment_modes")
    val paymentModes: List<PaymentMode?>?,
    @SerializedName("payment_statuses")
    val paymentStatuses: List<PaymentStatuse?>?,
    @SerializedName("play_order_cancellation_reasons")
    val playOrderCancellationReasons: List<PlayOrderCancellationReason?>?,
    @SerializedName("play_proof_types")
    val playProofTypes: List<PlayProofType?>?,
    @SerializedName("region_types")
    val regionTypes: List<RegionType?>?,
    @SerializedName("retailer_order_cancellation_reasons")
    val retailerOrderCancellationReasons: List<RetailerOrderCancellationReason?>?,
    @SerializedName("runner_order_cancellation_reasons")
    val runnerOrderCancellationReasons: List<RunnerOrderCancellationReason?>?,
    @SerializedName("runner_payment_modes")
    val runnerPaymentModes: List<RunnerPaymentMode?>?,
    @SerializedName("runner_payment_reschedule_reasons")
    val runnerPaymentRescheduleReasons: List<RunnerPaymentRescheduleReason?>?
) {
    data class BaseUnit(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class CategoryType(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class CompanyProofType(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class OfferType(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class OrderStatuse(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class PackUnit(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class PaymentMode(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class PaymentStatuse(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class PlayOrderCancellationReason(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class PlayProofType(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class RegionType(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class RetailerOrderCancellationReason(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class RunnerOrderCancellationReason(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class RunnerPaymentMode(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )

    data class RunnerPaymentRescheduleReason(
        @SerializedName("name")
        val name: String?,
        @SerializedName("value")
        val value: String?
    )
}