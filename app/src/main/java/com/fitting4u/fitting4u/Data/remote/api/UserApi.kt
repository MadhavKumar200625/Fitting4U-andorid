package com.fitting4u.fitting4u.Data.remote.api

import com.fitting4u.fitting4u.Data.remote.dto.user.BackendLoginResponse
import com.fitting4u.fitting4u.Data.remote.request_model.user.BackendLoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("/api/user")
    suspend fun loginWithPhone(@Body request: BackendLoginRequest): BackendLoginResponse

}