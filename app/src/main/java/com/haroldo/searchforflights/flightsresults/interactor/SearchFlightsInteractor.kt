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

@FlightsResultScope
class SearchFlightsInteractor @Inject constructor(
    private val gateway: SearchFlightsGateway,
    private val mapper: ItineraryResponseMapper,
    @IOScheduler private val scheduler: Scheduler,
    pollingUrlProvider: PollingUrlProvider
) {

    private val disposables = CompositeDisposable()

    init {
        pollingUrlProvider
            .pollingUrlChanges()
            .subscribe {
                request?.retry()
            }.addTo(disposables)
    }

    private var request: InMemoryFlowableRequest<List<Itinerary>>? = null

    fun events(): Observable<Event<List<Itinerary>>> {
        if (request == null) {
            request = InMemoryFlowableRequest(source())
        }

        return request!!.events()
    }

    fun retry() = request?.retry()

    private fun source(): Flowable<List<Itinerary>> {
        return gateway
            .fetchSearchFlightsResult()
            .repeatWhen { it.delay(1, TimeUnit.SECONDS, scheduler) }
            .takeUntil { it.status == Status.UpdatesComplete }
            .map { mapper.map(it.itineraries) }
            .map { itineraries ->
                return@map if (!itineraries.isEmpty()) {
                    itineraries.clientSideCalculations().sortedBy { it.price }
                } else {
                    itineraries
                }
            }
    }
}



