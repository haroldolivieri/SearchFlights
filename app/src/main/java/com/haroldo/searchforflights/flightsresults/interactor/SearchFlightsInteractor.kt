package com.haroldo.searchforflights.flightsresults.interactor

import com.haroldo.searchforflights.flightsresults.model.Itinerary
import com.haroldo.searchforflights.flightsresults.model.api.ApiResponseItinerary
import com.haroldo.searchforflights.flightsresults.model.api.ApiResponseSearch
import com.haroldo.searchforflights.flightsresults.model.api.SearchFlightsApiGateway
import com.haroldo.searchforflights.flightsresults.model.api.Status
import com.haroldo.searchforflights.flightsresults.model.mapper.ItineraryResponseMapper
import com.haroldo.searchforflights.request.Event
import com.haroldo.searchforflights.request.InMemoryCachedRequest
import io.reactivex.Flowable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

//needs scope
class SearchFlightsInteractor(
    private val gateway: SearchFlightsApiGateway,
    private val mapper: ItineraryResponseMapper
) {

    private var request: InMemoryCachedRequest<List<Itinerary>>? = null

    fun events(): Observable<Event<List<Itinerary>>> {
        if (request == null) {
            request = InMemoryCachedRequest(source())
        }

        return request!!.events()
    }

    fun retry() = request?.retry()

    fun dispose() = request?.dispose()

    private fun source(): Flowable<List<Itinerary>> {
        return gateway
            .fetchSearchFlightsResult()
            .repeatWhen { it.delay(1, TimeUnit.SECONDS) }
            .takeUntil { it.status == Status.UpdatesComplete }
            .map { mapper.map(it.itineraries) }
    }
}



