package com.rigle.servicehub.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SalesPerson(
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
    val username: String?,
    @SerializedName("mapped_brands")
    val mappedBrands: List<MappedBrand>?

) : Parcelable {
    fun getMappedBrandString(): String {
        val stringBuilder = StringBuilder()
        mappedBrands?.forEachIndexed { index, mappedBrand ->
            if (stringBuilder.isNotEmpty())
                stringBuilder.append(",")
            stringBuilder.append(mappedBrand.brand?.name)
        }
        return stringBuilder.toString()
    }
    @Parcelize
    data class MappedBrand(
        @SerializedName("brand")
        val brand: Brand?,
        @SerializedName("company")
        val company: Int?,
        @SerializedName("created_at")
        val createdAt: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("monthly_target")
        val monthlyTarget: Int?,
      /*  @SerializedName("pincodes")
        val pincodes: List<Any?>?,*/
        @SerializedName("update_url")
        val updateUrl: String?,
        @SerializedName("updated_at")
        val updatedAt: String?,
        @SerializedName("user")
        val user: Int?
    ) : Parcelable {
        @Parcelize
        data class Brand(
            @SerializedName("code")
            val code: String?,
            @SerializedName("company")
            val company: Int?,
            @SerializedName("created_at")
            val createdAt: String?,
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
            val updatedAt: String?
        ): Parcelable
    }
}