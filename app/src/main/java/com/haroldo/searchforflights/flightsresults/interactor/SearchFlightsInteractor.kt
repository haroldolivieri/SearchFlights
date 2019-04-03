package com.haroldo.searchforflights.flightsresults.interactor

import com.haroldo.searchforflights.di.FlightsResultScope
import com.haroldo.searchforflights.di.IOScheduler
import com.haroldo.searchforflights.flightsresults.gateway.SearchFlightsGateway
import com.haroldo.searchforflights.model.Itinerary
import com.haroldo.searchforflights.model.api.Status
import com.haroldo.searchforflights.model.mapper.ItineraryResponseMapper
import com.haroldo.searchforflights.model.mapper.clientSideCalculations
import com.haroldo.searchforflights.network.PollingUrlProvider
import com.haroldo.searchforflights.request.Event
import com.haroldo.searchforflights.request.InMemoryFlowableRequest
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val PAGE_SIZE = 50

@FlightsResultScope
class SearchFlightsInteractor @Inject constructor(
    private val gateway: SearchFlightsGateway,
    private val mapper: ItineraryResponseMapper,
    @IOScheduler private val scheduler: Scheduler,
    pollingUrlProvider: PollingUrlProvider
) {

    private val disposables = CompositeDisposable()
    private var lastPageIndex = -1
    private var oldItineraries: MutableSet<Itinerary> = mutableSetOf()

    init {
        pollingUrlProvider
            .pollingUrlChanges()
            .subscribe {
                request?.retry()
            }.addTo(disposables)
    }

    private var request: InMemoryFlowableRequest<Pair<List<Itinerary>, Boolean>>? = null

    fun events(pageIndex: Int): Observable<Event<Pair<List<Itinerary>, Boolean>>> {
        if (request == null || lastPageIndex != pageIndex) {
            this.lastPageIndex = pageIndex
            request = InMemoryFlowableRequest(source(pageIndex))
        }

        return request!!.events()
    }

    fun retry() = request?.retry()

    private fun source(pageIndex: Int): Flowable<Pair<List<Itinerary>, Boolean>> {
        return gateway
            .fetchSearchFlightsResult(pageIndex, PAGE_SIZE)
            .repeatWhen { it.delay(1, TimeUnit.SECONDS, scheduler) }
            .takeUntil { it.status == Status.UpdatesComplete }
            .map { response ->

                val rawItineraries = mapper.map(response.itineraries)
                oldItineraries.addAll(rawItineraries)

                val resultItineraries = oldItineraries
                    .toList()
                    .clientSideCalculations()
                    .sortedBy { it.price }

                Pair(
                    resultItineraries,
                    resultItineraries.isEmpty()
                )
            }
    }
}
