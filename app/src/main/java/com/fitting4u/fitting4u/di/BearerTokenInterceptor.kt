package com.lavasago.client.di

import com.fitting4u.fitting4u.common.Constants.BEARER_TOKEN
import okhttp3.Interceptor
import okhttp3.Response

class BearerTokenInterceptor :Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Authorization","Bearer $BEARER_TOKEN ")

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}