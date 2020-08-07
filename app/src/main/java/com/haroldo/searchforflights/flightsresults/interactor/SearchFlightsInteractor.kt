package com.haroldo.searchforflights.flightsresults.interactor

import com.haroldo.searchforflights.di.FlightsResultScope
import com.haroldo.searchforflights.di.IOScheduler
import com.haroldo.searchforflights.flightsresults.gateway.SearchFlightsGateway
import com.haroldo.searchforflights.model.Itinerary
import com.haroldo.searchforflights.model.api.ApiResponseSearch
import com.haroldo.searchforflights.model.api.Status
import com.haroldo.searchforflights.model.mapper.ItineraryResponseMapper
import com.haroldo.searchforflights.model.mapper.clientSideCalculations
import com.haroldo.searchforflights.request.Event
import com.haroldo.searchforflights.request.InMemoryFlowableRequest
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val PAGE_SIZE = 8
private const val FIRST_PAGE_INDEX = 0

@FlightsResultScope
class SearchFlightsInteractor @Inject constructor(
    private val gateway: SearchFlightsGateway,
    private val mapper: ItineraryResponseMapper,
    @IOScheduler private val scheduler: Scheduler
) {

    private var pageIndex = -1
    private var lastPageAlreadyLoaded = false
    private var oldItineraries: MutableSet<Itinerary> = mutableSetOf()

    private var request: InMemoryFlowableRequest<Pair<List<Itinerary>, Boolean>>? = null

    fun events(): Observable<Event<Pair<List<Itinerary>, Boolean>>> {
        if (request == null) {
            request = InMemoryFlowableRequest(source(FIRST_PAGE_INDEX))
        }

        return request!!.events()
    }

    fun nextPage() {
        if (!lastPageAlreadyLoaded && request?.lastEventType() != Event.Type.LOADING) {
            request?.changeSource(source(++pageIndex))
        }
    }

    fun retry() = request?.retry()

    private fun source(pageIndex: Int): Flowable<Pair<List<Itinerary>, Boolean>> {
        this.pageIndex = pageIndex
        val search = gateway.fetchSearchFlightsResult(pageIndex, PAGE_SIZE)
        val stream = getCorrectStream(pageIndex, search)

        return stream
            .map { itineraries ->
                if (itineraries.isEmpty()) {
                    this.lastPageAlreadyLoaded = true
                }

                Pair(
                    itineraries.clientSideCalculations(),
                    this.lastPageAlreadyLoaded
                )
            }
    }

    private fun getCorrectStream(
        pageIndex: Int,
        search: Single<ApiResponseSearch>
    ): Flowable<List<Itinerary>> {
        return when (pageIndex) {
            FIRST_PAGE_INDEX -> {
                search
                    .repeatWhen { it.delay(1, TimeUnit.SECONDS, scheduler) }
                    .takeUntil { it.status == Status.UpdatesComplete }
                    .map { response ->
                        getListDependingOnStatus(response)
                    }
            }
            else -> {
                search.toFlowable().map {
                    oldItineraries.addAll(mapper.map(it.itineraries))
                    oldItineraries.toList()
                }
            }
        }
    }

    private fun getListDependingOnStatus(
        response: ApiResponseSearch
    ): List<Itinerary> {
        val rawItineraries = mapper.map(response.itineraries)

        return if (response.status == Status.UpdatesComplete) {
            oldItineraries.addAll(rawItineraries)
            oldItineraries.toList()
        } else {
            rawItineraries
        }
    }
}
