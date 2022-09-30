package com.rigle.servicehub.data.model


import com.google.gson.annotations.SerializedName

data class MyAccountResponse(
    @SerializedName("account_status")
    val accountStatus: String?,
    @SerializedName("address_line")
    val addressLine: Any?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("full_address")
    val fullAddress: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("lat")
    val lat: Any?,
    @SerializedName("locality")
    val locality: Any?,
    @SerializedName("logo")
    val logo: Any?,
    @SerializedName("long")
    val long: Any?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("pincode")
    val pincode: String?,
    @SerializedName("short_address")
    val shortAddress: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("update_url")
    val updateUrl: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("users")
    val users: List<User>?,
    @SerializedName("extra")
    val extra: Extra?,
) {
    data class User(
        @SerializedName("company")
        val company: Int?,
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("date_joined")
        val dateJoined: String?,
        @SerializedName("designation")
        val designation: Any?,
        @SerializedName("email")
        val email: Any?,
        @SerializedName("first_name")
        val firstName: String?,
        @SerializedName("full_name")
        val fullName: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("image")
        val image: Any?,
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
        @SerializedName("middle_name")
        val middleName: String?,
        @SerializedName("mobile")
        val mobile: String?,
        @SerializedName("permissions")
        val permissions: List<String>?,
        @SerializedName("role")
        val role: String?,
        @SerializedName("update_url")
        val updateUrl: String?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        @SerializedName("user_id")
        val userId: Any?,
        @SerializedName("username")
        val username: Any?
    )
}