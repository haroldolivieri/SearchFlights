package com.haroldo.searchforflights.flightsresults.gateway

import com.haroldo.searchforflights.model.api.ApiResponseSearch
import com.haroldo.searchforflights.network.PollingUrlProvider
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*
import javax.inject.Inject

class SearchFlightsGateway @Inject constructor(
    private val api: Api,
    private val poolingUrlProvider: PollingUrlProvider
) {

//    fun createSearchFlightsSession(api) = api.createSession()

    fun fetchSearchFlightsResult() = api.poolingResults(poolingUrlProvider.get())

    interface Api {
        @POST("/pricing/v1.0")
        @FormUrlEncoded
        fun createSession(
            @Field("cabinclass") cabinClass: String,
            @Field("country") country: String,
            @Field("currency") currency: String,
            @Field("locale") locale: String,
            @Field("locationSchema") locationSchema: String,
            @Field("originplace") originPlace: String,
            @Field("destinationplace") destinationPlace: String,
            @Field("outbounddate") outboundDate: String,
            @Field("inbounddate") inboundDate: String,
            @Field("adults") adults: Int,
            @Field("children") children: Int,
            @Field("infants") infants: Int
        ): Completable

        @GET
        fun poolingResults(@Url url: String): Single<ApiResponseSearch>
    }
}
