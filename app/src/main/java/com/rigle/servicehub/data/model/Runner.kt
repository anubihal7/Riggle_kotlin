package com.rigle.servicehub.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Runner(
    @SerializedName("company")
    val company: Int?,
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
    val manager: String?,
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
    val userId: String?,
    @SerializedName("username")
    val username: String?
) : Parcelable