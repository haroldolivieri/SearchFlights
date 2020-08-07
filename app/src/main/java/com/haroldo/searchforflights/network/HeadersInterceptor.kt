package com.haroldo.searchforflights.network

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val CREATE_SESSION_URL = "http://partners.api.skyscanner.net/apiservices/pricing/v1.0"
private const val METHOD_POST = "POST"

class HeadersInterceptor @Inject constructor(private val poolingUrlProvider: PollingUrlProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        if (originalRequest.method == METHOD_POST &&
            originalRequest.url == CREATE_SESSION_URL.toHttpUrlOrNull()
        ) {
            response.headers["Location"]?.let {
                poolingUrlProvider.set(it)
            }
        }

        return response
    }
}