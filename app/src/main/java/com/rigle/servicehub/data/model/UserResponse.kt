package com.rigle.servicehub.data.model


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("max_age")
    val maxAge: Int?,
    @SerializedName("session_id")
    val sessionId: String?,
    @SerializedName("user")
    val user: User?
)

data class User(
    @SerializedName("company")
    var company: Company?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("date_joined")
    val dateJoined: String?,
    @SerializedName("designation")
    val designation: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("full_name")
    var fullName: String?,
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
    val manager: String?,
    @SerializedName("middle_name")
    val middleName: String?,
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("permissions")
    val permissions: List<String?>?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("update_url")
    val updateUrl: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("username")
    val username: String?
)

data class Company(
    @SerializedName("address_line")
    val addressLine: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("account_status")
    val accountStatus: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("extra")
    val extra: Extra?,
    @SerializedName("full_address")
    val fullAddress: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("locality")
    val locality: String?,
    @SerializedName("logo")
    val logo: String?,
    @SerializedName("long")
    val long: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("pincode")
    val pincode: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("update_url")
    val updateUrl: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)

data class Extra(
    @SerializedName("company")
    val company: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("gst_number")
    val gstNumber: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_gst_invoice")
    val isGstInvoice: Boolean?,
    @SerializedName("proof_file")
    val proofFile: String?,
    @SerializedName("proof_type")
    val proofType: String?,
    @SerializedName("qr_code_text")
    val qrCodeText: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("created_by")
    val createdBy: Any?,
    @SerializedName("is_plan_active")
    val isPlanActive: Boolean?,
    @SerializedName("plan_duration")
    val planDuration: Int?,
    @SerializedName("plan_end_date")
    val planEndDate: String?,
    @SerializedName("plan_start_date")
    val planStartDate: String?,
    @SerializedName("plan_type")
    val planType: String?,
    @SerializedName("store_type")
    val storeType: Any?,

    )


