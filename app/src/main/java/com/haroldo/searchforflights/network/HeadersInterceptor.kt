package com.haroldo.searchforflights.network

import com.haroldo.searchforflights.di.ApiKey
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeadersInterceptor @Inject constructor(private val poolingUrlProvider: PollingUrlProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        response.headers().get("Location")?.let {
            poolingUrlProvider.set(it)
        }

        return response
    }
}

private const val API_KEY = "apiKey"

class AddApiKeyInterceptor @Inject constructor(@ApiKey private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(API_KEY, apiKey)
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}