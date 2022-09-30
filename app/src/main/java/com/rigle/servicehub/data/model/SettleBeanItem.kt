package com.rigle.servicehub.data.model
import com.google.gson.annotations.SerializedName

data class SettleBeanItem(
    @SerializedName("delivery_boy")
    val deliveryBoy: Runner?,
    @SerializedName("settlement_data")
    val settlementData: SettlementData?
)

/*
data class DeliveryBoy(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("date_joined")
    val dateJoined: String?,
    @SerializedName("doc_id")
    val docId: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("is_staff")
    val isStaff: Boolean?,
    @SerializedName("is_superuser")
    val isSuperuser: Boolean?,
    @SerializedName("last_login")
    val lastLogin: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("manager")
    val manager: Any?,
    @SerializedName("master_ops")
    val masterOps: Int?,
    @SerializedName("middle_name")
    val middleName: String?,
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("permissions")
    val permissions: List<Any>?,
    @SerializedName("retailer")
    val retailer: Int?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("service_hub")
    val serviceHub: Int?,
    @SerializedName("update_url")
    val updateUrl: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("username")
    val username: String?
)
*/

data class SettlementData(
    @SerializedName("order_value")
    val orderValue: Int?,
    @SerializedName("settlements")
    val settlements: List<Settlement>?
)

data class Settlement(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("payment_mode")
    val paymentMode: String?,
    @SerializedName("payment_mode_value")
    val paymentModeValue: String?,
    @SerializedName("settled_amount")
    val settledAmount: Float?,
    @SerializedName("unsettled_amount")
    val unsettledAmount: Float?
)