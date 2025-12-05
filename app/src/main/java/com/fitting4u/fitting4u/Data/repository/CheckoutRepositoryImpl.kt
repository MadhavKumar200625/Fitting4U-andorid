// CheckoutRepositoryImpl.kt
package com.fitting4u.fitting4u.Data.repository

import com.fitting4u.fitting4u.Data.remote.api.*
import com.fitting4u.fitting4u.Data.remote.dto.order.boutiques.nearby.NearbyBoutiquesResponse
import com.fitting4u.fitting4u.Data.remote.dto.order.create_order.CreateOrderResponseModel
import com.fitting4u.fitting4u.Data.remote.dto.order.place_order.PlaceOrderResponse
import com.fitting4u.fitting4u.Data.remote.dto.order.verify.VerifyResponse
import com.fitting4u.fitting4u.Data.remote.request_model.order.create_order.CreateOrderRequestModel
import com.fitting4u.fitting4u.Data.remote.request_model.order.place_order.ConfirmOrderRequest
import com.fitting4u.fitting4u.Data.remote.request_model.order.verify.VerifyRequestModel
import com.fitting4u.fitting4u.common.Resource
import com.fitting4u.fitting4u.domain.repository.CheckoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class CheckoutRepositoryImpl @Inject constructor(
    private val api: CheckoutApi
) : CheckoutRepository {
    override suspend fun createOrder(amount: Double): Result<CreateOrderResponseModel> {
        return try {
            Result.success(api.createOrder(CreateOrderRequestModel(amount)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyPayment(razorReq: VerifyRequestModel): Result<VerifyResponse> {
        return try {
            Result.success(api.verifyPayment(razorReq))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNearbyBoutiques(lat: Double, long: Double): Result<NearbyBoutiquesResponse> {
        return try {
            Result.success(api.getNearbyBoutiques(lat, long))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun confirmOrder(req: ConfirmOrderRequest): Result<PlaceOrderResponse> {
        return try {
            Result.success(api.confirmOrder(req))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLatLngFromPincode(pin: String): Resource<Pair<Double, Double>> {
        return withContext(Dispatchers.IO) {
            try {
                val urlString =
                    "https://nominatim.openstreetmap.org/search?format=json&q=$pin,India"

                val url = URL(urlString)
                val connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    setRequestProperty("User-Agent", "Fitting4U-App/1.0 (contact@fitting4u.com)")
                    connectTimeout = 8000
                    readTimeout = 8000
                }

                val response = connection.inputStream.bufferedReader().use { it.readText() }

                val arr = JSONArray(response)
                if (arr.length() == 0)
                    return@withContext Resource.Error("Invalid Pincode")

                val obj = arr.getJSONObject(0)
                val lat = obj.getDouble("lat")
                val lon = obj.getDouble("lon")

                Resource.Success(lat to lon)

            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error("Failed to fetch location → ${e.message}")
            }
        }
    }
}