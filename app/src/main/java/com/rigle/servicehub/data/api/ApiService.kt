package com.rigle.servicehub.data.api

import com.rigle.servicehub.data.model.*
import com.rigle.servicehub.data.network.base.PageResponse
import com.google.gson.JsonElement
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("user/auth/send_otp/")
    fun getOtp(@Body request: RequestBody): Single<JsonElement>

    @POST("core/offers/")
    fun addOffer(
        @Header("Authorization") authkey: String,
        @Body request: RequestBody
    ): Single<JsonElement>

    @GET("core/offers/")
    fun getOffer(
        @Header("Authorization") authkey: String
    ): Single<PageResponse<OfferBean>>

    @GET("core/stocks?expand=product&brand_view=true")
    fun getInventory(
        @Header("Authorization") authkey: String
    ): Single<List<Inventory>>

    @GET("core/stocks?expand=product")
    fun getStockById(
        @Header("Authorization") authkey: String,
        @Query("brand") id: String
    ): Single<PageResponse<Stock>>

    @GET("core/cart/?expand=products.product")
    fun getCart(
        @Header("Authorization") authkey: String,
        @QueryMap request: Map<String,String>
    ): Single<CartResponse>

    @POST("user/auth/verify_otp/")
    fun verifyOtp(@Body request: RequestBody): Single<UserResponse>

    @POST("user/users/")
    fun addUser(
        @Header("Authorization") authkey: String,
        @Body request: RequestBody
    ): Single<JsonElement>

    @POST("core/orders/")
    fun submitOrder(
        @Header("Authorization") authkey: String,
        @Body request: RequestBody
    ): Single<JsonElement>

    @PATCH("user/users/{id}/")
    fun updateRunner(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @Body request: RequestBody
    ): Single<JsonElement>

    @PATCH("core/orders/{id}/cancel/")
    fun cancelOrder(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @Body request: RequestBody
    ): Single<JsonElement>

    @PATCH("core/orders/{id}/update_products/")
    fun updateProducts(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @Body request: EditProductRequest
    ): Single<JsonElement>

    @PATCH("core/cart/{id}/update_products/")
    fun updateProductsQty(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @Body request: RequestBody
    ): Single<JsonElement>

    @PATCH("user/users/{id}/")
    fun updateSalesPerson(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @Body request: RequestBody
    ): Single<JsonElement>

    @PATCH("core/orders/{orderId}/runner/")
    fun assignRunner(
        @Header("Authorization") authkey: String,
        @Path("orderId") orderId: String,
        @Body request: RequestBody
    ): Single<JsonElement>


    @GET("user/auth/ping/")
    fun authPing(@Header("Authorization") authkey: String): Single<JsonElement>

    @GET("core/companies/{id}/plan_data/")
    fun planData(
        @Header("Authorization") authkey: String,
        @Path("id") id: String
    ): Single<PlanResponse>

    @GET("core/companies/{id}/runners_settlement/")
    fun runnersSettlement(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @QueryMap map: Map<String, String>
    ): Single<List<RunnerResponse>>

    @GET("core/companies/{id}/runners_settlement/")
    fun runnersSettlementDetail(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @QueryMap map: Map<String, String>
    ): Single<RunnerResponse>

    @GET("core/companies/{id}/settlement_report/")
    fun getSettlements(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @QueryMap map: Map<String, String>
    ): Single<JsonElement>

    @GET("core/brands/?is_active=true")
    fun getBrands(
        @Header("Authorization") authkey: String,
    ): Single<BrandsResponse>

    @GET("core/companies/{id}/?expand=users,extra")
    fun myAccount(
        @Header("Authorization") authkey: String,
        @Path("id") id: String
    ): Single<MyAccountResponse>

    @GET("core/companies/{id}/dashboard")
    fun dashboard(
        @Header("Authorization") authkey: String,
        @Path("id") id: String
    ): Single<DashboardResponse>

    @GET("core/companies/{id}/?expand=company,extra")
    fun getCompany(
        @Header("Authorization") authkey: String,
        @Path("id") id: String
    ): Single<Company>

    @GET("user/service-hubs/{id}/settlement_summary")
    fun settlementSummary(
        @Header("Authorization") authkey: String,
        @Path("id") id: String
    ): Single<List<SettleBeanItem>>

    @GET("core/stocks/low_stock_brands")
    fun lowStockBrands(@Header("Authorization") authkey: String): Single<List<StockResponse>>

    @GET("core/constants/")
    fun getRigleConstants(): Single<RigleConstantsResponse>

    @GET("user/users/?role=runner")
    fun getRunner(@Header("Authorization") authkey: String): Single<PageResponse<Runner>>

    @GET("user/users/?role=sales_man&expand=mapped_brands.brand")
    fun getSalesPerson(@Header("Authorization") authkey: String): Single<PageResponse<SalesPerson>>

    @GET("user/users/{id}")
    fun getRunner(@Header("Authorization") authkey: String, @Path("id") id: String): Single<Runner>

    @GET("user/users/{id}")
    fun getSalesPerson(
        @Header("Authorization") authkey: String,
        @Path("id") id: String
    ): Single<SalesPerson>

    @GET("core/orders")
    fun getOrder(
        @Header("Authorization") authkey: String,
        @QueryMap map: Map<String, String>
    ): Single<PageResponse<OrderBean>>

    @GET("core/orders/{id}")
    fun getOrder(
        @Header("Authorization") authkey: String,
        @Path("id") orderId: String,
        @QueryMap map: Map<String, String>
    ): Single<OrderBean>

    @JvmSuppressWildcards
    @PATCH("core/companies/{id}/")
    @Multipart
    fun updateBusinessProfile(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @PartMap map: Map<String, RequestBody>,
        @Part list: List<MultipartBody.Part>
    ): Single<Company>

    @JvmSuppressWildcards
    @PATCH("core/companies/{id}/")
    fun updateBusinessQrcodeProfile(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @Body map: RequestBody,
    ): Single<Company>

    @JvmSuppressWildcards
    @PATCH("core/companies/{id}/initiate_plan/")
    fun initiatePlan(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @Body map: RequestBody,
    ): Single<PaymentResponse>


    @JvmSuppressWildcards
    @Multipart
    @PATCH("user/users/{id}/")
    fun updateUser(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @Part("full_name") body: RequestBody
    ): Single<Company>

    @JvmSuppressWildcards
    @Multipart
    @PATCH("user/users/{id}/")
    fun updateUserInfo(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @PartMap map: Map<String, RequestBody>
    ): Single<JsonElement>


    @JvmSuppressWildcards
    @Multipart
    @PATCH("core/orders/{id}/confirm/")
    fun confirmOrder(
        @Header("Authorization") authkey: String,
        @Path("id") id: String,
        @PartMap map: Map<String, RequestBody>
    ): Single<JsonElement>

    @JvmSuppressWildcards
    @Multipart
    @PATCH("core/stocks/{id}/")
    fun updateStockProduct(
        @Header("Authorization") authkey: String,
        @Path("id") productId: String,
        @PartMap map: Map<String, RequestBody>
    ): Single<JsonElement>
}