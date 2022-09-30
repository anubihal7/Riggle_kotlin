package com.rigle.servicehub.data.repository

import com.rigle.servicehub.data.api.ApiService
import com.rigle.servicehub.data.local.SharedPrefManager
import com.rigle.servicehub.data.model.*
import com.rigle.servicehub.data.network.base.PageResponse
import com.google.gson.JsonElement
import io.reactivex.Single
import io.reactivex.functions.Consumer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class BaseRepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val sharedPrefManager: SharedPrefManager
) : BaseRepo {
    override fun submitOrder(request: JSONObject): Single<JsonElement> {
        return apiService.submitOrder(
            sharedPrefManager.getAuth(),
            request.toString().toRequestBody("application/json".toMediaType())
        )
    }

    override fun getCart(jsonObject: Map<String,String>): Single<CartResponse> {
        return apiService.getCart(
            sharedPrefManager.getAuth(),
            jsonObject
        )
    }

    override fun initiatePlan(id: String, jsonObject: JSONObject): Single<PaymentResponse> {
        return apiService.initiatePlan(
            sharedPrefManager.getAuth(),
            id,
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        )
    }

    override fun getCompany(id: String): Single<Company> {
        return apiService.getCompany(sharedPrefManager.getAuth(), id)
    }

    override fun getSettlements(id: String, map: Map<String, String>): Single<JsonElement> {
        return apiService.getSettlements(
            sharedPrefManager.getAuth(),
            id,
            map
        )
    }

    override fun runnersSettlement(
        id: String,
        map: Map<String, String>
    ): Single<List<RunnerResponse>> {
        return apiService.runnersSettlement(
            sharedPrefManager.getAuth(),
            id,
            map
        )
    }

    override fun runnersSettlementDetail(
        id: String,
        map: Map<String, String>
    ): Single<RunnerResponse> {
        return apiService.runnersSettlementDetail(
            sharedPrefManager.getAuth(),
            id,
            map
        )
    }

    override fun getInventory(): Single<List<Inventory>> {
        return apiService.getInventory(
            sharedPrefManager.getAuth()
        )
    }

    override fun getStockById(id: String): Single<PageResponse<Stock>> {
        return apiService.getStockById(
            sharedPrefManager.getAuth(), id
        )
    }

    override fun getOffer(): Single<PageResponse<OfferBean>> {
        return apiService.getOffer(
            sharedPrefManager.getAuth()
        )
    }

    override fun getBrands(): Single<BrandsResponse> {
        return apiService.getBrands(sharedPrefManager.getAuth())
    }

    override fun addOffer(jsonObject: JSONObject): Single<JsonElement> {
        return apiService.addOffer(
            sharedPrefManager.getAuth(),
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        )
    }

    override fun myAccount(id: String): Single<MyAccountResponse> {
        return apiService.myAccount(sharedPrefManager.getAuth(), id)
    }

    override fun planData(id: String): Single<PlanResponse> {
        return apiService.planData(sharedPrefManager.getAuth(), id)
    }

    override fun addUser(jsonObject: JSONObject): Single<JsonElement> {
        return apiService.addUser(
            sharedPrefManager.getAuth(),
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        )
    }

    override fun updateRunner(id: String, jsonObject: JSONObject): Single<JsonElement> {
        return apiService.updateRunner(
            sharedPrefManager.getAuth(),
            id,
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        )
    }

    override fun cancelOrder(id: String, jsonObject: JSONObject): Single<JsonElement> {
        return apiService.cancelOrder(
            sharedPrefManager.getAuth(),
            id,
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        )
    }

    override fun updateProducts(id: String, jsonObject: EditProductRequest): Single<JsonElement> {
        return apiService.updateProducts(
            sharedPrefManager.getAuth(),
            id,
            jsonObject
        )
    }

    override fun updateStockQty(id: String, jsonObject: JSONObject): Single<JsonElement> {
        return apiService.updateProductsQty(
            sharedPrefManager.getAuth(),
            id,
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        )
    }

    override fun updateSalesPerson(id: String, jsonObject: JSONObject): Single<JsonElement> {
        return apiService.updateSalesPerson(
            sharedPrefManager.getAuth(),
            id,
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        )
    }

    override fun assignRunner(orderId: String, jsonObject: JSONObject): Single<JsonElement> {
        return apiService.assignRunner(
            sharedPrefManager.getAuth(),
            orderId,
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        )
    }

    override fun getRigleConstants(): Single<RigleConstantsResponse> {
        return apiService.getRigleConstants().doOnSuccess {
            sharedPrefManager.saveRigleConstants(
                it
            )
        }

    }

    override fun getRunner(): Single<PageResponse<Runner>> {
        return apiService.getRunner(sharedPrefManager.getAuth())
    }

    override fun getSalesPerson(): Single<PageResponse<SalesPerson>> {
        return apiService.getSalesPerson(sharedPrefManager.getAuth())
    }

    override fun getSalesPerson(id: String): Single<SalesPerson> {
        return apiService.getSalesPerson(sharedPrefManager.getAuth(), id)
    }

    override fun getRunner(runnerId: String): Single<Runner> {
        return apiService.getRunner(sharedPrefManager.getAuth(), runnerId)
    }


    override fun settlementSummary(id: String): Single<List<SettleBeanItem>> {
        return apiService.settlementSummary(sharedPrefManager.getAuth(), id)
    }

    override fun authPing(): Single<JsonElement> {
        return apiService.authPing(sharedPrefManager.getAuth())
            .doOnSuccess(Consumer { it ->
                it?.let { it1 ->
                    it1.asJsonObject.get("session_id").asString?.let {
                        sharedPrefManager.saveSession(
                            it
                        )
                    }

                }
            })
    }


    override fun getOtp(jsonObject: JSONObject): Single<JsonElement> {
        return apiService.getOtp(
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        )
    }

    override fun verifyOtp(jsonObject: JSONObject): Single<UserResponse> {
        return apiService.verifyOtp(
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        ).doOnSuccess(Consumer {
            it?.let { it1 ->
                sharedPrefManager.saveUser(
                    it1.user
                )
                sharedPrefManager.saveSession(
                    it1.sessionId.toString()
                )
                sharedPrefManager.saveAuth(
                    it1.sessionId.toString()
                )
            }
        })
    }

    override fun dashboard(id: String): Single<DashboardResponse> {
        return apiService.dashboard(sharedPrefManager.getAuth(), id)
    }

    override fun lowStockBrands(id: String): Single<List<StockResponse>> {
        return apiService.lowStockBrands(sharedPrefManager.getAuth())
    }

    override fun getOrder(map: Map<String, String>): Single<PageResponse<OrderBean>> {
        return apiService.getOrder(sharedPrefManager.getAuth(), map)
    }

    override fun getOrder(orderId: String, map: Map<String, String>): Single<OrderBean> {
        return apiService.getOrder(sharedPrefManager.getAuth(), orderId, map)
    }

    override fun updateBusinessProfile(
        id: String,
        map: Map<String, String>,
        partList: List<LocalMedia>
    ): Single<Company> {
        val bodyMap = hashMapOf<String, RequestBody>()
        val parts = arrayListOf<MultipartBody.Part>()
        map.forEach {
            bodyMap[it.key] = it.value.toRequestBody("text/plain".toMediaType())
        }
        partList.forEach {
            MultipartBody.Part.createFormData(
                it.key,
                it.filePath,
                it.filePath.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
        }
        return apiService.updateBusinessProfile(
            sharedPrefManager.getAuth(),
            id,
            bodyMap,
            parts
        ).doOnSuccess {
            val user = sharedPrefManager.getCurrentUser()
            user?.company = it
            sharedPrefManager.saveUser(user)
        }
    }

    override fun updateQrCode(id: String, qrcode: String): Single<Company> {
        return apiService.updateBusinessQrcodeProfile(
            sharedPrefManager.getAuth(),
            id,
            qrcode.toRequestBody("application/json".toMediaType())
        ).doOnSuccess {
            val user = sharedPrefManager.getCurrentUser()
            user?.company = it
            sharedPrefManager.saveUser(user)
        }
    }

    /*  override fun updateBusinessProfile(
          id: String,
          map: Map<String, String>,
        partList:List<LocalMedia>
      ): Flowable<Resource<UploadBean>> {
          val bodyMap = hashMapOf<String, RequestBody>()
          map.forEach {
              bodyMap[it.key] = it.value.toRequestBody("text/plain".toMediaType())
          }
        *//*  return apiService.updateBusinessProfile(
            sharedPrefManager.getAuth(),
            id,
            bodyMap
        ).doOnSuccess {
            val user = sharedPrefManager.getCurrentUser()
            user?.retailer = it
            sharedPrefManager.saveUser(user)
        }*//*
          return Flowable.create({ emitter ->
              try {
                  val arrayList = ArrayList<MultipartBody.Part>()
                  partList.forEach { it ->
                      RetroUtils.createMultipartBody(it.filePath, it.key, emitter)?.let {
                          arrayList.add(it)
                      }
                  }

                  val bodyMap = hashMapOf<String, RequestBody>()
                  map.forEach {
                      bodyMap[it.key] = it.value.toRequestBody("text/plain".toMediaType())
                  }
                  val response =
                      apiService.updateBusinessProfile(sharedPrefManager.getAuth(), id, arrayList, bodyMap)
                          .blockingGet()
                  response?.let {
                      val user = sharedPrefManager.getCurrentUser()
                      //   user?.service_hub = it
                      sharedPrefManager.saveUser(user)
                  }
                  val uploadBean = UploadBean()
                  uploadBean.data = response
                  uploadBean.isUploaded = true
                  emitter.onNext(Resource.success(uploadBean, "Success"))
                  emitter.onComplete()
              } catch (e: Exception) {
                  emitter.tryOnError(e)
                  emitter.onNext(Resource.error(null, e.message.toString()))
              }
          }, BackpressureStrategy.LATEST)
    }
*/
    override fun updateUser(id: String, fullName: String): Single<Company> {
        return apiService.updateUser(
            sharedPrefManager.getAuth(),
            id,
            fullName.toRequestBody("text/plain".toMediaType())
        ).doOnSuccess {
            val user = sharedPrefManager.getCurrentUser()
            user?.fullName = fullName
            sharedPrefManager.saveUser(user)
        }
    }

    override fun updateUserInfo(id: String, map: Map<String, String>): Single<JsonElement> {
        val s = mutableMapOf<String, RequestBody>()
        map.forEach {
            s[it.key] = it.value.toRequestBody("text/plain".toMediaType())
        }
        return apiService.updateUserInfo(
            sharedPrefManager.getAuth(),
            id,
            s
        )
    }

    override fun confirmOrder(id: String, map: Map<String, String>): Single<JsonElement> {
        val s = mutableMapOf<String, RequestBody>()
        map.forEach {
            s[it.key] = it.value.toRequestBody("text/plain".toMediaType())
        }
        return apiService.confirmOrder(
            sharedPrefManager.getAuth(),
            id,
            s
        )
    }

    override fun updateStockProduct(id: String, map: Map<String, String>): Single<JsonElement> {
        val s = mutableMapOf<String, RequestBody>()
        map.forEach {
            s[it.key] = it.value.toRequestBody("text/plain".toMediaType())
        }
        return apiService.updateStockProduct(
            sharedPrefManager.getAuth(),
            id,
            s
        )
    }

}
