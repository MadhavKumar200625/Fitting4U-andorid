package com.fitting4u.fitting4u.Data.remote.api


import com.fitting4u.fitting4u.Data.remote.dto.Config.ConfigDto
import retrofit2.http.GET

interface ConfigApi {

    @GET("/api/site-config")
    suspend fun getConfig(): ConfigDto
}