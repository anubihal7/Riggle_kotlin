package com.rigle.servicehub.data.repository

import com.rigle.servicehub.data.model.*
import com.rigle.servicehub.data.network.base.PageResponse
import com.google.gson.JsonElement
import io.reactivex.Single
import org.json.JSONObject

interface BaseRepo {
    fun submitOrder(request: JSONObject): Single<JsonElement>
    fun getCart(jsonObject:  Map<String,String>): Single<CartResponse>
    fun initiatePlan(id: String, jsonObject: JSONObject): Single<PaymentResponse>
    fun getCompany(id: String): Single<Company>
    fun getSettlements(id: String, map: Map<String, String>): Single<JsonElement>
    fun runnersSettlement(id: String, map: Map<String, String>): Single<List<RunnerResponse>>
    fun runnersSettlementDetail(id: String, map: Map<String, String>): Single<RunnerResponse>
    fun getInventory(): Single<List<Inventory>>
    fun getStockById(id: String): Single<PageResponse<Stock>>
    fun getOffer(
    ): Single<PageResponse<OfferBean>>

    fun getBrands(): Single<BrandsResponse>
    fun addOffer(
        jsonObject: JSONObject
    ): Single<JsonElement>

    fun myAccount(id: String): Single<MyAccountResponse>
    fun planData(id: String): Single<PlanResponse>
    fun addUser(jsonObject: JSONObject): Single<JsonElement>
    fun updateRunner(id: String, jsonObject: JSONObject): Single<JsonElement>
    fun cancelOrder(id: String, jsonObject: JSONObject): Single<JsonElement>
    fun updateProducts(id: String, jsonObject: EditProductRequest): Single<JsonElement>
    fun updateStockQty(id: String, jsonObject: JSONObject): Single<JsonElement>
    fun updateSalesPerson(id: String, jsonObject: JSONObject): Single<JsonElement>
    fun assignRunner(orderId: String, jsonObject: JSONObject): Single<JsonElement>
    fun getRigleConstants(): Single<RigleConstantsResponse>
    fun getRunner(): Single<PageResponse<Runner>>
    fun getSalesPerson(): Single<PageResponse<SalesPerson>>
    fun getSalesPerson(id: String): Single<SalesPerson>
    fun getRunner(runnerId: String): Single<Runner>
    fun settlementSummary(id: String): Single<List<SettleBeanItem>>
    fun authPing(): Single<JsonElement>
    fun getOtp(jsonObject: JSONObject): Single<JsonElement>
    fun verifyOtp(jsonObject: JSONObject): Single<UserResponse>
    fun dashboard(id: String): Single<DashboardResponse>
    fun lowStockBrands(id: String): Single<List<StockResponse>>
    fun getOrder(map: Map<String, String>): Single<PageResponse<OrderBean>>
    fun getOrder(orderId: String, map: Map<String, String>): Single<OrderBean>
    fun updateBusinessProfile(
        id: String,
        map: Map<String, String>,
        partList: List<LocalMedia>
    ): Single<Company>

    fun updateQrCode(
        id: String,
        qrcode: String
    ): Single<Company>

    fun updateUser(
        id: String,
        fullName: String
    ): Single<Company>

    fun updateUserInfo(
        id: String,
        map: Map<String, String>
    ): Single<JsonElement>

    fun confirmOrder(
        id: String,
        map: Map<String, String>
    ): Single<JsonElement>

    fun updateStockProduct(
        id: String,
        map: Map<String, String>
    ): Single<JsonElement>
}