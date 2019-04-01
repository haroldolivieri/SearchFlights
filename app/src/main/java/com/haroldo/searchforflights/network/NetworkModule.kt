package com.haroldo.searchforflights.network

import com.google.gson.GsonBuilder
import com.haroldo.searchforflights.di.ApplicationScope
import com.haroldo.searchforflights.flightsresults.gateway.SearchFlightsGateway
import com.haroldo.searchforflights.model.jsonparser.SearchFlightsResponseTypeAdapter
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT = 20L
private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
private const val BASE_URL = "http://partners.api.skyscanner.net/apiservices/"

@Module
class NetworkModule {

    @Provides
    @ApplicationScope
    fun provideProductService(retrofit: Retrofit): SearchFlightsGateway.Api =
        retrofit.create(SearchFlightsGateway.Api::class.java)

    @Provides
    @ApplicationScope
    fun provideOkHttpClient(builder: OkHttpClient.Builder): OkHttpClient = builder.build()

    @Provides
    @ApplicationScope
    fun provideOkHttpClientBuilder(
        headersInterceptor: HeadersInterceptor,
        addApiKeyInterceptor: AddApiKeyInterceptor
    ): OkHttpClient.Builder =
        OkHttpClient().newBuilder()
            .addInterceptor(headersInterceptor)
            .addInterceptor(addApiKeyInterceptor)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)

    @Provides
    @ApplicationScope
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder()
            .registerTypeAdapterFactory(SearchFlightsResponseTypeAdapter())
            .setDateFormat(DATE_FORMAT)
            .create()

        return GsonConverterFactory.create(gson)
    }

    @Provides
    @ApplicationScope
    fun provideRetrofit(gsonConverterFactory: GsonConverterFactory, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
}