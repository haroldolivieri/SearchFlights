package com.haroldo.searchforflights.flightsresults.model.api

import io.reactivex.Single

interface SearchFlightsApiGateway {
    fun createSearchFlightsSession()
    fun fetchSearchFlightsResult(): Single<ApiResponseSearch>
}